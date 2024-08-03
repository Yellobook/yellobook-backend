package com.yellobook.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MakeOrderResponse {
    private Long orderId;

    @Builder
    private MakeOrderResponse(Long orderId){
        this.orderId = orderId;
    }
}
