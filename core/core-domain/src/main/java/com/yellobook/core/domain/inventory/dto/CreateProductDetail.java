package com.yellobook.core.domain.inventory.dto;

public record CreateProductDetail(
        String name,
        String subProduct,
        Integer sku,
        Integer purchasePrice,
        Integer salePrice,
        Integer amount
) {
}
