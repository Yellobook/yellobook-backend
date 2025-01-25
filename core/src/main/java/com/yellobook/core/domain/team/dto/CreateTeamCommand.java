package com.yellobook.core.domain.team.dto;

import com.yellobook.core.domain.common.TeamMemberRole;

public record CreateTeamCommand(
        String name,
        String phoneNumber,
        String address,
        TeamMemberRole role
) {
}
