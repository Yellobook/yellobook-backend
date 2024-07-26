package com.yellobook.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MakeOrderRequest {
    private String memo;
    private LocalDate date;
    private Integer orderAmount;


//    private Integer view;
//    private OrderStatus orderStatus;
//
//    private Product product;
//    private Member member;
//    private Team team;
}
