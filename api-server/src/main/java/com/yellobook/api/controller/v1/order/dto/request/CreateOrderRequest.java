package com.yellobook.api.controller.v1.order.dto.request;

import com.yellobook.core.domain.order.NewOrder;
import com.yellobook.core.domain.order.NewOrder.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "주문 생성 요청")
public record CreateOrderRequest(
        @Schema(description = "주문 상품 목록", required = true)
        @NotEmpty(message = "주문 상품 목록은 비어 있을 수 없습니다.")
        @Valid
        List<OrderItemRequest> orderItems
) {
    @Schema(description = "주문 상품 정보")
    public record OrderItemRequest(
            @Schema(description = "상품 ID", example = "1024", required = true)
            @NotNull(message = "상품 ID는 필수입니다.")
            @Min(value = 1, message = "상품 ID는 1 이상이어야 합니다.")
            Long productId,

            @Schema(description = "주문 수량", example = "2", required = true)
            @NotNull(message = "주문 수량은 필수입니다.")
            @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다.")
            Integer quantity
    ) {
    }

    public NewOrder toNewOrder(Long storeId) {
        return new NewOrder(
                storeId,
                orderItems.stream()
                        .map(orderItem -> new OrderItem(
                                        orderItem.productId,
                                        orderItem.quantity
                                )
                        )
                        .toList()
        );
    }
}
