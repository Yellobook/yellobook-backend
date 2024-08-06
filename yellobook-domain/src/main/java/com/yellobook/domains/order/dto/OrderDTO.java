package com.yellobook.domains.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private LocalDate date;
    private String writer;
    private String productName;
    private String subProductName;
    private Integer amount;
    private String memo;
}
