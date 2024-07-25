package com.yellobook.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제품 응답 - 요청 DTO")
@Getter
public class AddProductResponse {
    @Schema(description = "생성한 상품 id")
    private Long productId;

    @Builder
    public AddProductResponse(Long productId){
        this.productId = productId;
    }
}
