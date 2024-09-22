package com.yellobook.domains.order.dto.query;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record QueryOrder(
        LocalDate date,
        String writer,
        String productName,
        String subProductName,
        Integer amount,
        String memo
) {
}
