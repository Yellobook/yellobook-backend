package com.yellobook.core.domain.team.dto;


import com.yellobook.core.enums.TeamMemberRole;

public record InvitationInfo(
        Long teamId,
        TeamMemberRole role
) {
    public InvitationInfo(String teamIdStr, String roleDescription) {
        this(
                Long.valueOf(teamIdStr),
                TeamMemberRole.fromDisplayName(roleDescription)
        );
    }
}
