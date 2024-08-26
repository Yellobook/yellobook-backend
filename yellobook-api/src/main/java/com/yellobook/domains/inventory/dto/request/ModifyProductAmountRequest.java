package com.yellobook.domains.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "제품 수량 수정 - 요청 DTO")
@Builder
public record ModifyProductAmountRequest(
        @Schema(description = "수정할 수량")
        @NotNull @Min(value = 0, message = "수정할 수량은 0보다 커야합니다.")
        Integer amount
) {
}
