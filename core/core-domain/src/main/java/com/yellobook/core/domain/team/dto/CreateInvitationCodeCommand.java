package com.yellobook.core.domain.team.dto;

import com.yellobook.core.enums.TeamMemberRole;

public record CreateInvitationCodeCommand(
        Long teamId,
        TeamMemberRole role
) {
}
