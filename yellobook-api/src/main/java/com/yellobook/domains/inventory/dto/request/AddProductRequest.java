package com.yellobook.domains.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "제품 추가 - 요청 DTO")
@Builder
public record AddProductRequest(
        @Schema(description = "제품명")
        @NotBlank
        String name,
        @Schema(description = "하위 제품")
        @NotBlank
        String subProduct,
        @Schema(description = "품번")
        @NotNull
        Integer sku,
        @Schema(description = "구매가")
        @NotNull
        Integer purchasePrice,
        @Schema(description = "판매가")
        @NotNull
        Integer salePrice,
        @Schema(description = "재고")
        @NotNull
        Integer amount
) {

}
