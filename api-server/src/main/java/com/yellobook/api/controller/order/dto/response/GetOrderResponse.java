package com.yellobook.api.controller.order.dto.response;

import java.time.LocalDate;

public record GetOrderResponse(
        LocalDate date,
        String writer,
        String productName,
        String subProductName,
        Integer amount,
        String memo) {

}
