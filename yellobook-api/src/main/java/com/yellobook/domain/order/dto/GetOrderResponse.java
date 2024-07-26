package com.yellobook.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetOrderResponse {
    private LocalDate date;
    private String writer;
    private String productName;
    private String subProductName;
    private Integer amount;
    private String memo;

    @Builder
    private GetOrderResponse(LocalDate date, String writer, String productName, String subProductName, Integer amount, String memo){
        this.date = date;
        this.writer = writer;
        this.productName = productName;
        this.subProductName = subProductName;
        this.amount = amount;
        this.memo = memo;
    }

}
