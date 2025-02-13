package com.yellobook.core.domain.order;

import java.util.List;

public record NewOrder(
        long storeId,
        List<OrderItem> orderItems
) {
    public record OrderItem(
            long productId,
            int quantity
    ) {
    }
}

