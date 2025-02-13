package com.yellobook.core.domain.order;

public record StoreOrder(
        long storeId,
        long orderId
) {
    public static StoreOrder of(long storeId, long orderId) {
        return new StoreOrder(storeId, orderId);
    }
}
