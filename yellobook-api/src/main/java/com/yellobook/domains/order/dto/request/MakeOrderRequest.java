package com.yellobook.domains.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MakeOrderRequest(
        @NotNull(message = "productId를 입력해주세요")
        Long productId,
        String memo,
        @NotNull(message = "주문 날짜를 입력해주세요")
        LocalDate date,
        @NotNull(message = "주문 수량을 입력해주세요")
        Integer orderAmount
){
}
