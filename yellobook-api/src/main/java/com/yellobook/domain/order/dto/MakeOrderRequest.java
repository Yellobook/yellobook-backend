package com.yellobook.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MakeOrderRequest {
    private Long productId;
    private String memo;
    private LocalDate date;
    private Integer orderAmount;
}
