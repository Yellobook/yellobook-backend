package com.yellobook.domains.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderDTO {
    private LocalDate date;
    private String writer;
    private String productName;
    private String subProductName;
    private Integer amount;
    private String memo;
}
