package com.yellobook.domains.order.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MakeOrderRequest(
        Long productId,
        String memo,
        LocalDate date,
        Integer orderAmount
){
}
