package com.yellobook.domains.team.dto.query;

import com.yellobook.common.enums.TeamMemberRole;
import lombok.Builder;

@Builder
public record QueryMemberJoinTeam(
        TeamMemberRole role,
        Long teamId,
        String teamName
) {
}
