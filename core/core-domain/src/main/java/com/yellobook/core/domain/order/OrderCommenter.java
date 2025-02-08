package com.yellobook.core.domain.order;

import com.yellobook.core.enums.TeamMemberRole;

public record OrderCommenter(
        Long commenterId,
        String nickname,
        TeamMemberRole role
) {
}
