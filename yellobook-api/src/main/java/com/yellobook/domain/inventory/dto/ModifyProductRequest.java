package com.yellobook.domain.inventory.dto;

import lombok.*;

//TODO
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyProductRequest {
    private String name;
    private String subProduct;
    private Integer sku;
    private Integer purchasePrice;
    private Integer salePrice;
    private Integer amount;
}
