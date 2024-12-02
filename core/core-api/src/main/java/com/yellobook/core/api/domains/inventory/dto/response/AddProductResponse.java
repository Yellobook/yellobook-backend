package com.yellobook.core.api.domains.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "제품 응답 - 요청 DTO")
@Builder
public record AddProductResponse(
        @Schema(description = "생성한 상품 id")
        Long productId
) {
}
