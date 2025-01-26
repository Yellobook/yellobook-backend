package com.yellobook.core.domain.team.dto;

import com.yellobook.core.domain.common.TeamMemberRole;

public record CreateInvitationCodeCommand(
        Long teamId,
        TeamMemberRole role
) {
}
