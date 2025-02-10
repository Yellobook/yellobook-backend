package com.yellobook.core.domain.team.dto;

public record CreateTeamCommand(
        String name,
        String description,
        String phoneNumber,
        String address,
        Boolean searchable
) {
}
