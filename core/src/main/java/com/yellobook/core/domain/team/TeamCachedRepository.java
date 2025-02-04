package com.yellobook.core.domain.team;

import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public interface TeamCachedRepository {
    void saveInvitationCode(String key, String value, long time, TimeUnit unit);

    void saveCurrentTeam(Long teamId, Long memberId, String role);

    String readTeamIdByCode(String key);

    void applyTeam(String key, Long memberId);

    boolean isTeamJoinRequestExist(String key, Long memberId);

    void removeTeamJoinRequest(String key, Long memberId);

    void requestOrdererConversion(String key, Long memberId);

    boolean isOrdererConversionRequestExist(String key, Long memberId);

    void removeOrdererConversionRequest(String key, Long memberId);
}
