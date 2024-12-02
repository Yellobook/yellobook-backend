package com.yellobook.core.domains.inventory.dto.query;

import lombok.Builder;

@Builder
public record QueryProduct(
        Long productId,
        String name,
        String subProduct,
        Integer sku,
        Integer purchasePrice,
        Integer salePrice,
        Integer amount
) {
}
