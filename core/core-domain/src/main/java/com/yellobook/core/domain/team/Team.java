package com.yellobook.core.domain.team;

//aggregate root
public record Team(
        Long teamId,
        String name,
        String description,
        String phoneNumber,
        String address,
        Boolean isSearchable
) {
}
