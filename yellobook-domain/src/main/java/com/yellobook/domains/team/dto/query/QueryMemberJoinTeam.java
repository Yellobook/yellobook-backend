package com.yellobook.domains.team.dto.query;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.Builder;

@Builder
public record QueryMemberJoinTeam(
        MemberTeamRole role,
        String teamName
) {
}
