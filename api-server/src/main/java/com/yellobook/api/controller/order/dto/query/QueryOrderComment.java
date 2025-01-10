package com.yellobook.api.controller.order.dto.query;

import com.yellobook.TeamMemberRole;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record QueryOrderComment(
        Long commentId,
        TeamMemberRole role,
        String content,
        LocalDateTime createdAt
) {

}
