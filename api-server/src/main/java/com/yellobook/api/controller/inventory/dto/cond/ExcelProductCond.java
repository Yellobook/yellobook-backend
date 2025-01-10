package com.yellobook.api.controller.inventory.dto.cond;

import lombok.Builder;

@Builder
public record ExcelProductCond(
        String name,
        String subProduct,
        Integer sku,
        Integer purchasePrice,
        Integer salePrice,
        Integer amount
) {
}
