package com.yellobook.core.domain.order;

import com.yellobook.core.domain.common.TeamMemberRole;

public record OrderCommenter(
        Long commenterId,
        String nickname,
        TeamMemberRole role
) {
}
