package com.yellobook.core.api.domains.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "다수의 제품 정보 조회 - 응답 DTO")
@Builder
public record GetProductsResponse(
        @Schema(description = "제품")
        List<ProductInfo> products
) {

    @Builder
    public record ProductInfo(
            @Schema(description = "제품 id")
            Long productId,
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

}
