package com.yellobook.core.domain.inventory.dto;

public record CreateProductCommend(
        Long inventoryId,
        String name,
        String subProduct,
        int sku,
        int purchasePrice,
        int salePrice,
        int amount
) {
    public CreateProductCommend(Long inventoryId, CreateProductDetail productCond) {
        this(
                inventoryId,
                productCond.name(),
                productCond.subProduct(),
                productCond.sku(),
                productCond.purchasePrice(),
                productCond.salePrice(),
                productCond.amount()
        );
    }
}
