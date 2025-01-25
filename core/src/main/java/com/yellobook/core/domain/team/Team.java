package com.yellobook.core.domain.team;

//aggregate root
public record Team(
        Long teamId,
        String name,
        String phoneNumber,
        String address
) {
}
