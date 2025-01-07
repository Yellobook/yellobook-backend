package com.yellobook.core.domain.team;

public record Team(
        Long teamId,
        String name,
        String phoneNumber,
        String address
) {
}
