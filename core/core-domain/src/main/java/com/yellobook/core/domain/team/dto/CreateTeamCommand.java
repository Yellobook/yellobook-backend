package com.yellobook.core.domain.team.dto;


import com.yellobook.core.enums.TeamMemberRole;

public record CreateTeamCommand(
        String name,
        String description,
        String phoneNumber,
        String address,
        TeamMemberRole role,
        Boolean isSearchable
) {
}
