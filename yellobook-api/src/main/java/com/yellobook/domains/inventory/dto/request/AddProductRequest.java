package com.yellobook.domains.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "제품 추가 - 요청 DTO")
@Builder
public record AddProductRequest(
        @Schema(description = "제품명")
        String name,
        @Schema(description = "하위 제품")
        String subProduct,
        @Schema(description = "품번")
        Integer sku,
        @Schema(description = "구매가")
        Integer purchasePrice,
        @Schema(description = "판매가")
        Integer salePrice,
        @Schema(description = "재고")
        Integer amount
) {

}
