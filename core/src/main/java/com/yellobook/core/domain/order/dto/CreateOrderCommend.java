package com.yellobook.core.domain.order.dto;

import com.yellobook.core.domain.order.OrderStatus;
import java.time.LocalDate;

public record CreateOrderCommend(
        String memo,
        LocalDate date,
        Integer orderAmount,
        Long productId,
        Integer view,
        OrderStatus status,
        Long ordererId,
        Long teamId
) {
}
