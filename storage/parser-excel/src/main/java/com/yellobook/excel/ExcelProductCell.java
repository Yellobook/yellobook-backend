package com.yellobook.excel;

public record ExcelProductCell(
        String name,
        String subProduct,
        Integer sku,
        Integer purchasePrice,
        Integer salePrice,
        Integer amount
) {

}
