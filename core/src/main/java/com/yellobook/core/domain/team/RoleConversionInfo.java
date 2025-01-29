package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;

public record RoleConversionInfo(
        Long conversionId,
        Long teamId,
        Long memberId,
        TeamMemberRole requestRole,
        ChangeRoleStatus status
) {
}
