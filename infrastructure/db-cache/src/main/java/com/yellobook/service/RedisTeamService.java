package com.yellobook.service;

import java.util.List;
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
     * 존재하는 키 인지 확인
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 키에 저장된 values 조회
     */
    public List<String> getValueList(String key, long start, long end) {
        ListOperations<String, String> valueOps = redisTemplate.opsForList();
        List<String> values = valueOps.range(key, 0, 1);
        return values;
    }

    /**
     * 키에 저장된 value 조회
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue()
                .get(key);
    }

    /**
     * key & value 저장
     */
    public void setValueWithExpiry(String key, String value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue()
                .set(key, value, timeout, timeUnit);
    }

    public String generateTeamKey(Long memberId) {
        return "member:team:" + memberId;
    }
}
