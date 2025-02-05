package com.yellobook.api.controller.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "재고, 제품 생성")
public record AddInventoryResponse(
        @Schema(description = "생성된 재고 id", example = "1L")
        Long inventoryId,
        @Schema(description = "생성된 제품 id", example = "[1L, 2L, ...]")
        List<Long> productIds
) {
}
