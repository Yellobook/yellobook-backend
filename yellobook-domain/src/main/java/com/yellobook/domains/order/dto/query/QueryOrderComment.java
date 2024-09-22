package com.yellobook.domains.order.dto.query;

import com.yellobook.common.enums.MemberTeamRole;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record QueryOrderComment(
        Long commentId,
        MemberTeamRole role,
        String content,
        LocalDateTime createdAt
) {

}
