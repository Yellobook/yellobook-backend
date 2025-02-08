package com.yellobook.core.domain.order.dto;

public record CreateOrderCommentCommend(
        Long orderId,
        Long ordererId,
        String comment
) {
}
