package com.yellobook.common.utils;

import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamUtil {
    private final StringRedisTemplate stringRedisTemplate;
    private final ParticipantRepository participantRepository;
    /**
     * 사용자가 위치한 팀 id 를 저장한다.
     */
    public void saveMemberTeam(Long memberId, Long teamId) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateTeamKey(memberId);
        valueOps.set(key, teamId.toString());
    }

    /**
     * 사용자가 위치한 팀 id 를 가져온다.
     */
    public Long getCurrentTeam(Long memberId) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = generateTeamKey(memberId);
        String teamIdValue = valueOps.get(key);

        if (teamIdValue != null) {
            return Long.parseLong(teamIdValue);
        } else {
            Participant participant = participantRepository.findFirstByMemberIdOrderByCreatedAtAsc(memberId)
                    .orElseThrow(() -> new CustomException(TeamErrorCode.USER_NOT_JOINED_ANY_TEAM));
            Long teamId = participant.getTeam().getId();
            // redis 에 저장
            saveMemberTeam(memberId, teamId);
            return teamId;
        }
    }

    private String generateTeamKey(Long memberId) {
        return "member:team:" + memberId;
    }
}
