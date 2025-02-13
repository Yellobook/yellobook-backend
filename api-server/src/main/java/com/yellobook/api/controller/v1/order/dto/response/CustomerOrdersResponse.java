package com.yellobook.api.controller.v1.order.dto.response;

import com.yellobook.core.domain.order.OrdersResult;
import com.yellobook.core.enums.OrderStatus;
import com.yellobook.core.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "주문 목록 조회 응답")
public record CustomerOrdersResponse(
        @Schema(description = "주문 ID", example = "1024")
        long orderId,

        @Schema(description = "가게 ID", example = "10")
        long storeId,

        @Schema(description = "주문번호 (ORD-XXXX-YYYY)", example = "ORD-KX7P2B-4F9A")
        String orderNumber,

        @Schema(description = "총 주문 금액", example = "35000")
        int totalPrice,

        @Schema(description = "주문한 상품 개수", example = "3")
        int productCount,

        @Schema(description = "주문 생성 날짜", example = "2024-02-12T12:30:45")
        LocalDateTime orderCreatedAt,

        @Schema(description = "주문 상태", example = "APPROVED")
        OrderStatus orderStatus,

        @Schema(description = "거래 방식", example = "DELIVERY")
        TransactionType transactionType
) {
    public static List<CustomerOrdersResponse> fromList(List<OrdersResult> results) {
        return results.stream()
                .map(result -> new CustomerOrdersResponse(
                        result.orderId(),
                        result.storeId(),
                        result.orderNumber(),
                        result.totalPrice(),
                        result.productCount(),
                        result.createdAt(),
                        result.orderStatus(),
                        result.transactionType()
                ))
                .toList();
    }
}

