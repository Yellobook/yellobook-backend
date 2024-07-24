package com.yellobook.domains.inventory.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDTO {
    private Long productId;
    private String name;
    private String subProduct;
    private Integer sku;
    private Integer purchasePrice;
    private Integer salePrice;
    private Integer amount;
}
