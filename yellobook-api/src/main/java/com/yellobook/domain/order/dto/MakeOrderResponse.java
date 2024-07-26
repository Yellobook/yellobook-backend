package com.yellobook.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MakeOrderResponse {
    private Long orderId;

    @Builder
    public MakeOrderResponse(Long orderId){
        this.orderId = orderId;
    }
}
