package com.yellobook.domains.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "제품 수량 수정 - 요청 DTO")
@Builder
public record ModifyProductAmountRequest(
        @Schema(description = "수정할 수량")
        Integer amount
) {
}
