package com.yellobook.service;

import com.yellobook.core.domain.team.TeamCachedRepository;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTeamDao implements TeamCachedRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisTeamDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveInvitationCode(String key, String value, long time, TimeUnit unit) {
        setValueWithExpiry(key, value, time, unit);
    }

    @Override
    public String readTeamIdByCode(String key) {
        return getTeamIdByCode(key);
    }

    public void delete(String code) {
        redisTemplate.delete(code);
    }

    // 초대 코드 존재 여부 확인
    public Boolean exists(String code) {
        return redisTemplate.hasKey(code);
    }

    @Override
    public void saveCurrentTeam(Long teamId, Long memberId, String role) {
        ListOperations<String, String> valueOps = redisTemplate.opsForList();
        String key = generateTeamKey(memberId);
        // 기존 정보가 있다면 삭제
        delete(key);
        // 현재 위치한 팀 정보로 갱신
        valueOps.rightPush(key, teamId.toString());
        valueOps.rightPush(key, role);
    }

    private String generateTeamKey(Long memberId) {
        return "member:team:" + memberId;
    }

    /**
     * key & value 저장
     */
    public void setValueWithExpiry(String key, String value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue()
                .set(key, value, timeout, timeUnit);
    }

    /**
     * 키에 저장된 value 조회
     */
    public String getTeamIdByCode(String key) {
        return redisTemplate.opsForValue()
                .get(key);
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
