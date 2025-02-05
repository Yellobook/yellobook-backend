package com.yellobook.api.controller.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "제품 추가 - 요청 DTO")
public record AddProductRequest(
        @Schema(description = "제품명")
        @NotBlank
        String name,
        @Schema(description = "하위 제품")
        @NotBlank
        String subProduct,
        @Schema(description = "품번")
        @NotNull @Min(value = 0, message = "품번은 0 이상이여야 합니다.")
        Integer sku,
        @Schema(description = "구매가")
        @NotNull @Min(value = 0, message = "구매가는 0원 이상이여야 합니다.")
        Integer purchasePrice,
        @Schema(description = "판매가")
        @NotNull @Min(value = 0, message = "판매가는 0원 이상이여야 합니다.")
        Integer salePrice,
        @Schema(description = "재고")
        @NotNull @Min(value = 0, message = "수량은 0개 이상이여야 합니다.")
        Integer amount
) {

}
