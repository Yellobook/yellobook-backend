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
    public void save(String key, String value, long time, TimeUnit unit) {
        setValueWithExpiry(key, value, time, unit);
    }

    @Override
    public Object read(String key) {
        return getValue(key);
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

    public String generateTeamKey(Long memberId) {
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
    public Object getValue(String key) {
        return redisTemplate.opsForValue()
                .get(key);
    }
}
