package com.yellobook.api.controller.order.dto.query;

import java.time.LocalDate;

public record QueryOrder(
        LocalDate date,
        String writer,
        String productName,
        String subProductName,
        Integer amount,
        String memo
) {
}
