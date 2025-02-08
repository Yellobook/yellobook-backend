package com.yellobook.core.domain.order.dto;

import java.time.LocalDate;

public record CreateOrderPayload(
        String memo,
        LocalDate date,
        Integer orderAmount,
        Long productId
) {
}
