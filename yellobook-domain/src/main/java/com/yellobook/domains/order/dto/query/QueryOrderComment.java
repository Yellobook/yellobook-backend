package com.yellobook.domains.order.dto.query;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QueryOrderComment(
        Long commentId,
        MemberTeamRole role,
        String content,
        LocalDateTime createdAt
) {

}
