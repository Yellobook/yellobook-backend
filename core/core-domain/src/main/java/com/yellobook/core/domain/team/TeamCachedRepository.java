package com.yellobook.core.domain.team;

import com.yellobook.core.domain.team.dto.InvitationInfo;
import com.yellobook.core.enums.TeamMemberRole;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamCachedRepository {
    InvitationInfo readTeamIdAndRoleByCode(String key);

    void applyTeam(String key, Long memberId);

    boolean isTeamJoinRequestExist(String key, Long memberId);

    void removeTeamJoinRequest(String key, Long memberId);

    void requestOrdererConversion(String key, Long memberId);

    boolean isOrdererConversionRequestExist(String key, Long memberId);

    void removeOrdererConversionRequest(String key, Long memberId);

    void saveInvitationCode(String key, Long teamId, TeamMemberRole inviteRole, long validMinute);
}
