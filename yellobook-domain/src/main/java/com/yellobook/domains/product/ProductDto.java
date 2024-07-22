package com.yellobook.domains.product;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {
    private Long productId;
    private String name;
    private String subProduct;
    private Integer sku;
    private Integer purchasePrice;
    private Integer salePrice;
    private Integer amount;
}
