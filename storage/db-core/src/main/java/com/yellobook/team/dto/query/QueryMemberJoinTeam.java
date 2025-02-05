package com.yellobook.team.dto.query;

import com.yellobook.core.domain.common.TeamMemberRole;
import lombok.Builder;

@Builder
public record QueryMemberJoinTeam(
        TeamMemberRole role,
        Long teamId,
        String teamName
) {
}
