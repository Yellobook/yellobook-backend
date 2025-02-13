package com.yellobook.core.domain.order;

import com.yellobook.core.enums.TransactionType;
import java.time.LocalDateTime;
import java.util.List;

public record Order(
        long orderId,
        List<OrderItem> orderItems,
        LocalDateTime createdAt,
        TransactionType transactionType,
        long totalPrice
) {
    public record OrderItem(
            long productId,
            long price,
            long quantity,
            String productName
    ) {
    }
}
