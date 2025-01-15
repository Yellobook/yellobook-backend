package com.yellobook.core.domain.inventory.dto;

import lombok.Builder;

@Builder
public record CreateProductDetail(
        String name,
        String subProduct,
        Integer sku,
        Integer purchasePrice,
        Integer salePrice,
        Integer amount
) {
}
