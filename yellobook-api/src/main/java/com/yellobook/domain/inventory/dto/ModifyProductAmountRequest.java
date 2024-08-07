package com.yellobook.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "제품 수량 수정 - 요청 DTO")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ModifyProductAmountRequest {
    @Schema(description = "수정할 수량")
    private Integer amount;
}
