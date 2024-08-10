package com.yellobook.domains.order.dto.query;

import lombok.Builder;

import java.time.LocalDate;

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
