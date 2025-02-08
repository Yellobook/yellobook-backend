package com.yellobook.core.domain.inventory;

public record Product(
        Long productId,
        Long inventoryId,
        String name,
        String subProduct,
        String sku,
        int purchasePrice,
        int salePrice,
        int amount
) {
}
