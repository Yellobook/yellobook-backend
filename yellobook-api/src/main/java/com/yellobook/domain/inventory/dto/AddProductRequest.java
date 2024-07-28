package com.yellobook.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "제품 추가 - 요청 DTO")
@Getter
@NoArgsConstructor
public class AddProductRequest {
    @Schema(description = "제품명")
    private String name;
    @Schema(description = "하위 제품")
    private String subProduct;
    @Schema(description = "품번")
    private Integer sku;
    @Schema(description = "구매가")
    private Integer purchasePrice;
    @Schema(description = "판매가")
    private Integer salePrice;
    @Schema(description = "재고")
    private Integer amount;
}
