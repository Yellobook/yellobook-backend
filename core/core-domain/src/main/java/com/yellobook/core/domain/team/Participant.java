package com.yellobook.core.domain.team;


import com.yellobook.core.enums.TeamMemberRole;

public record Participant(
        Long memberId,
        String nickname,
        TeamMemberRole role
) {
}
