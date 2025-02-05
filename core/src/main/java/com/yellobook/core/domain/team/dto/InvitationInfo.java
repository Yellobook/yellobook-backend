package com.yellobook.core.domain.team.dto;

import com.yellobook.core.domain.common.TeamMemberRole;

public record InvitationInfo(
        Long teamId,
        TeamMemberRole role
) {
    public InvitationInfo(String teamIdStr, String roleDescription) {
        this(
                Long.valueOf(teamIdStr),
                TeamMemberRole.fromDescription(roleDescription)
        );
    }
}
