package com.yellobook.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MakeOrderRequest {
    private Long productId;
    private String memo;
    private LocalDate date;
    private Integer orderAmount;
}
