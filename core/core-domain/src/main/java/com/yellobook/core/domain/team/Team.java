package com.yellobook.core.domain.team;

//aggregate root
public record Team(
        Long teamId,
        String teamName,
        String teamDescription,
        String sellerName,
        String memberCount
) {
}
