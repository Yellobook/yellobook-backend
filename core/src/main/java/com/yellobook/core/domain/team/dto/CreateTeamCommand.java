package com.yellobook.core.domain.team.dto;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.team.Searchable;

public record CreateTeamCommand(
        String name,
        String phoneNumber,
        String address,
        TeamMemberRole role,
        Searchable searchable
) {
}
