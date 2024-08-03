package com.yellobook.domain.auth.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTeamService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ParticipantRepository participantRepository;

    /**
     * 사용자가 위치한 팀 id 를 업데이트 또는 저장한다.
     */
    public void setMemberCurrentTeam(Long memberId, Long teamId, String memberTeamRoleName) {
        ListOperations<String, Object> valueOps = redisTemplate.opsForList();
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
            Long teamId = participant.getTeam().getId();
            MemberTeamRole memberTeamRole = participant.getRole();
            // redis 에 저장
            setMemberCurrentTeam(memberId, teamId, memberTeamRole.name());
            return TeamMemberVO.of(memberId, teamId, memberTeamRole);
        }
        //
        ListOperations<String, Object> valueOps = redisTemplate.opsForList();
        Long teamIdValue = (Long) valueOps.index(key, 0);
        String role =  (String) valueOps.index(key, 1);
        MemberTeamRole memberTeamRole = MemberTeamRole.valueOf(role);
        return TeamMemberVO.of(memberId, teamIdValue, memberTeamRole);
    }

    private String generateTeamKey(Long memberId) {
        return "member:team:" + memberId;
    }
}
