package com.yellobook.domains.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record GetSubProductNameResponse(
        @Schema(description = "하위 제품 정보")
        List<SubProductInfo> subProducts
) {
    @Builder
    public record SubProductInfo(
            @Schema(description = "제품 id, 주문 시 해당 id를 전달해주세요", defaultValue = "1")
            Long productId,
            @Schema(description = "하위 제품의 이름", defaultValue = "노란색")
            String subProductName
    ){
    }

}
