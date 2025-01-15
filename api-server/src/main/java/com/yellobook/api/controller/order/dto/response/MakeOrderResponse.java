package com.yellobook.api.controller.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MakeOrderResponse(
        @Schema(description = "주문 Id", example = "1")
        Long orderId
) {
}