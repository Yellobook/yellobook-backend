package com.yellobook.service;

import com.yellobook.core.domain.team.TeamCachedRepository;
import com.yellobook.core.domain.team.dto.InvitationInfo;
import com.yellobook.core.enums.TeamMemberRole;
import java.time.Duration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisTeamDao implements TeamCachedRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final HashOperations<String, String, String> hashOperation;
    private static final String FIELD_TEAM_ID = "teamId";
    private static final String FILED_ROLE = "role";

    public RedisTeamDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperation = redisTemplate.opsForHash();
    }

    @Override
    public void saveInvitationCode(String key, Long teamId, TeamMemberRole inviteRole, long validMinute) {
        hashOperation.put(key, FIELD_TEAM_ID, String.valueOf(teamId));
        hashOperation.put(key, FILED_ROLE, inviteRole.getDisplayName());

        redisTemplate.expire(key, Duration.ofSeconds(validMinute));
    }

    @Override
    public InvitationInfo readTeamIdAndRoleByCode(String key) {
        String teamIdStr = hashOperation.get(key, FIELD_TEAM_ID);
        String roleDescription = hashOperation.get(key, FILED_ROLE);
        if (teamIdStr == null || roleDescription == null) {
            return null;
        }
        return new InvitationInfo(teamIdStr, roleDescription);
    }

    @Override
    public void applyTeam(String key, Long memberId) {
        addToSet(key, String.valueOf(memberId));
    }

    @Override
    public boolean isTeamJoinRequestExist(String key, Long memberId) {
        return isValueInSet(key, String.valueOf(memberId));
    }

    @Override
    public void removeTeamJoinRequest(String key, Long memberId) {
        removeFromSet(key, String.valueOf(memberId));
    }

    @Override
    public void requestOrdererConversion(String key, Long memberId) {
        addToSet(key, String.valueOf(memberId));
    }

    @Override
    public boolean isOrdererConversionRequestExist(String key, Long memberId) {
        return isValueInSet(key, String.valueOf(memberId));
    }

    @Override
    public void removeOrdererConversionRequest(String key, Long memberId) {
        removeFromSet(key, String.valueOf(memberId));
    }

    private void addToSet(String key, String value) {
        redisTemplate.opsForSet()
                .add(key, value);
    }

    private void removeFromSet(String key, String value) {
        redisTemplate.opsForSet()
                .remove(key, value);
    }

    private boolean isValueInSet(String key, String value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet()
                .isMember(key, value));
    }


}
