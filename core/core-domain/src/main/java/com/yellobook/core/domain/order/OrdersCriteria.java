package com.yellobook.core.domain.order;

public record OrdersCriteria(
        long storeId,
        Long cursorOrderId,
        long size
) {
}
