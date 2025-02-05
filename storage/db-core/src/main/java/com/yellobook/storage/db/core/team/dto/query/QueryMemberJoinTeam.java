package com.yellobook.storage.db.core.team.dto.query;

import com.yellobook.core.domain.common.TeamMemberRole;

public record QueryMemberJoinTeam(
        TeamMemberRole role,
        Long teamId,
        String teamName
) {
}
