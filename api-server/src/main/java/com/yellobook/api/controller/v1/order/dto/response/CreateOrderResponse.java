package com.yellobook.api.controller.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 생성 응답")
public record CreateOrderResponse(
        @Schema(description = "주문 ID", example = "1001")
        long orderId
) {
    public static CreateOrderResponse of(long orderId) {
        return new CreateOrderResponse(orderId);
    }
}
