package com.yellobook.core.domain.team.dto;

import com.yellobook.core.domain.common.TeamMemberRole;

public record InvitationCodeInfo(
        Long teamId,
        TeamMemberRole role
) {
}
