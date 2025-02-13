package com.yellobook.api.controller.v1.order.dto.response;

import com.yellobook.core.domain.order.Order;
import com.yellobook.core.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "주문 상세 조회 응답")
public record OrderDetailResponse(
        @Schema(description = "주문 ID", example = "1001")
        long orderId,

        @Schema(description = "주문한 상품 목록")
        List<OrderItemResponse> orderItems,

        @Schema(description = "주문 생성 일시", example = "2024-02-12T14:30:00")
        LocalDateTime createdAt,

        @Schema(description = "거래 방식 (DELIVERY 또는 DIRECT)", example = "DELIVERY")
        TransactionType transactionType,

        @Schema(description = "총 주문 금액", example = "35000")
        long totalPrice
) {
    @Schema(description = "주문 상품 정보")
    public record OrderItemResponse(
            @Schema(description = "상품 ID", example = "1024")
            long productId,

            @Schema(description = "상품 가격", example = "15000")
            long price,

            @Schema(description = "주문 수량", example = "2")
            long quantity,

            @Schema(description = "상품명", example = "무선 이어폰")
            String productName
    ) {
    }

    public static OrderDetailResponse of(Order order) {
        return new OrderDetailResponse(
                order.orderId(),
                order.orderItems()
                        .stream()
                        .map(orderItem -> new OrderItemResponse(
                                orderItem.productId(),
                                orderItem.price(),
                                orderItem.quantity(),
                                orderItem.productName()
                        ))
                        .toList(),
                order.createdAt(),
                order.transactionType(),
                order.totalPrice()
        );
    }
}
