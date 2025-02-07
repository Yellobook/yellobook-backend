package com.yellobook.core.domain.team.dto;

import com.yellobook.core.enums.TeamMemberRole;

public record InvitationCodeInfo(
        Long teamId,
        TeamMemberRole role
) {
}
