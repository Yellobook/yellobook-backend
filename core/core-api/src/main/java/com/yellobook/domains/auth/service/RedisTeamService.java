package com.yellobook.domains.auth.service;

import com.yellobook.domains.auth.dto.InvitationResponse;
import com.yellobook.support.error.code.CommonErrorCode;
import com.yellobook.support.error.code.TeamErrorCode;
import com.yellobook.support.error.exception.CustomException;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.TeamMemberRole;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisTeamService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ParticipantRepository participantRepository;
    private final HttpServletRequest request;

    /**
     * 사용자가 위치한 팀 id 를 업데이트 또는 저장한다.
     */
    public void setMemberCurrentTeam(Long memberId, Long teamId, String memberTeamRoleName) {
        ListOperations<String, String> valueOps = redisTemplate.opsForList();
        String key = generateTeamKey(memberId);
        // 기존 정보가 있다면 삭제
        redisTemplate.delete(key);
        // 현재 위치한 팀 정보로 갱신
        valueOps.rightPush(key, teamId.toString());
        valueOps.rightPush(key, memberTeamRoleName);
    }

    /**
     * 사용자가 위치함 팀 정보를 불러온다.
     */
    public TeamMemberVO getCurrentTeamMember(Long memberId) {
        String key = generateTeamKey(memberId);

        // 팀에대한 사용자가 존재하지 않는다면
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 사용자가 참여한 팀 목록을 조회
            Participant participant = participantRepository.findFirstByMemberIdOrderByCreatedAtAsc(memberId)
                    .orElseThrow(() -> new CustomException(TeamErrorCode.USER_NOT_JOINED_ANY_TEAM));
            Long teamId = participant.getTeam()
                    .getId();
            TeamMemberRole teamMemberRole = participant.getTeamMemberRole();
            // redis 에 저장
            setMemberCurrentTeam(memberId, teamId, teamMemberRole.name());
            return TeamMemberVO.of(memberId, teamId, teamMemberRole);
        }
        ListOperations<String, String> valueOps = redisTemplate.opsForList();
        List<String> values = valueOps.range(key, 0, 1);
        if (values != null && values.size() == 2) {
            Long teamId = Long.valueOf(values.get(0));
            TeamMemberRole teamMemberRole = TeamMemberRole.valueOf(values.get(1));
            return TeamMemberVO.of(memberId, teamId, teamMemberRole);
        }
        log.error("[TEAM_ERROR] 사용자 ID {}가 위치한 팀을 조회할 때 예상치 못한 값이 REDIS 에 들어가 있음: {}", memberId, values);
        throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }


    private String generateTeamKey(Long memberId) {
        return "member:team:" + memberId;
    }

    public String generateInvitationUrl(Long teamId, TeamMemberRole role) {
        String code = UUID.randomUUID()
                .toString();
        String key = generateInvitaionKey(code);
        String value = teamId + ":" + role;

        // 15분 간 유효한 링크 설정
        //호스트 별로 다르게!
        //키를 도메인에 맞춰서
        redisTemplate.opsForValue()
                .set(key, value, 15, TimeUnit.MINUTES);
        String invitationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return invitationUrl + "/api/v1/invitation?code=" + code;
    }

    public InvitationResponse getInvitationInfo(String key) {
        Object value = redisTemplate.opsForValue()
                .get("team:invite:" + key);
        if (value == null) {
            throw new CustomException(TeamErrorCode.INVITATION_NOT_FOUND);
        }

        // value 형식이 "teamId:role" 인지 확인하고 분리
        String stringValue = value.toString();
        String[] parts = stringValue.split(":");
        if (parts.length != 2) {
            throw new CustomException(TeamErrorCode.INVALID_INVITATION);
        }

        Long teamId = Long.parseLong(parts[0]);
        TeamMemberRole role = TeamMemberRole.valueOf(parts[1]);

        return new InvitationResponse(teamId, role);
    }

    private String generateInvitaionKey(String code) {
        return "team:invite:" + code;
    }
}
