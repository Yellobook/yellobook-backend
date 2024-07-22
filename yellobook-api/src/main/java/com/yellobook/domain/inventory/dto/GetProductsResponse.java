package com.yellobook.domain.inventory.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetProductsResponse {
    private List<ProductInfo> products;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductInfo{
        private Long productId;
        private String name;
        private String subProduct;
        private Integer sku;
        private Integer purchasePrice;
        private Integer salePrice;
        private Integer amount;
    }
}
