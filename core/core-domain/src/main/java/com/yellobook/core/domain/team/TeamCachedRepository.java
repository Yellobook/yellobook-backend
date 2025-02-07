package com.yellobook.core.domain.team;

import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public interface TeamCachedRepository {
    void save(String key, String value, long time, TimeUnit unit);

    void saveCurrentTeam(Long teamId, Long memberId, String role);

    Object read(String key);
}
