package com.yellobook.domains.inventory.dto;

import lombok.*;

@Getter
public class ProductDTO {
    private final Long productId;
    private final String name;
    private final String subProduct;
    private final Integer sku;
    private final Integer purchasePrice;
    private final Integer salePrice;
    private final Integer amount;

    public ProductDTO(Long productId, String name, String subProduct, Integer sku, Integer purchasePrice, Integer salePrice, Integer amount){
        this.productId = productId;
        this.name = name;
        this.subProduct = subProduct;
        this.sku = sku;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.amount = amount;
    }

}
