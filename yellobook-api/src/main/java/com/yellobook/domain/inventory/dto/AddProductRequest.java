package com.yellobook.domain.inventory.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductRequest {
    private String name;
    private String subProduct;
    private Integer sku;
    private Integer purchasePrice;
    private Integer salePrice;
    private Integer amount;
}
