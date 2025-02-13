package com.yellobook.core.domain.order;

import com.yellobook.core.enums.OrderStatus;
import com.yellobook.core.enums.TransactionType;
import java.time.LocalDateTime;

public record OrdersResult(
        long orderId,
        long storeId,
        int totalPrice,
        int productCount,
        String orderNumber,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        TransactionType transactionType,
        Long customerId,
        String customerName
) {
}
