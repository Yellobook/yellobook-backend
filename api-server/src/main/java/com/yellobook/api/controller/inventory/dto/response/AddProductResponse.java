package com.yellobook.api.controller.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "제품 응답 - 요청 DTO")
public record AddProductResponse(
        @Schema(description = "생성한 상품 id")
        Long productId
) {
}
