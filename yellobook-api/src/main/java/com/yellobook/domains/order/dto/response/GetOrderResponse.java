package com.yellobook.domains.order.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetOrderResponse (
    LocalDate date,
    String writer,
    String productName,
    String subProductName,
    Integer amount,
    String memo){

}
