package com.yellobook.core.domain.member;

public record JoinedTeamResult(
        Long teamId,
        String teamName,
        String teamDescription,
        String myRole,
        String sellerName,
        int memberCount
) {
}
