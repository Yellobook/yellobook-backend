package com.yellobook.core.domain.order;

public record Product(
        Long productId,
        String productName,
        String subProduct,
        int amount
) {
}
