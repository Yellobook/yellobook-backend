package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;

public record Participant(
        Long memberId,
        String nickname,
        TeamMemberRole role
) {
}
