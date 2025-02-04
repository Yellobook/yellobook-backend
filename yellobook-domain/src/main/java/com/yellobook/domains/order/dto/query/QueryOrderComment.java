package com.yellobook.domains.order.dto.query;

import com.yellobook.common.enums.TeamMemberRole;
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
