package com.yellobook.domain.order.dto.response;

import lombok.Builder;

@Builder
public record MakeOrderResponse (
    Long orderId
){
}
