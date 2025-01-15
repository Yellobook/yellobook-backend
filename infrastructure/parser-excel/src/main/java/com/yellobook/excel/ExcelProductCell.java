package com.yellobook.excel;

import lombok.Builder;

@Builder
public record ExcelProductCell(
        String name,
        String subProduct,
        Integer sku,
        Integer purchasePrice,
        Integer salePrice,
        Integer amount
) {

}
