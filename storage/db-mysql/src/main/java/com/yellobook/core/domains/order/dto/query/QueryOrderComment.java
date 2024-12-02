package com.yellobook.core.domains.order.dto.query;

import com.yellobook.core.core.enums.TeamMemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QueryOrderComment(
        Long commentId,
        TeamMemberRole role,
        String content,
        LocalDateTime createdAt
) {

}
