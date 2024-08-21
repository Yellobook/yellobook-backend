package com.yellobook.domains.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MakeOrderRequest(
        @Schema(description = "제품Id", example = "1")
        @NotNull(message = "productId를 입력해주세요")
        Long productId,
        @Schema(description = "메모", example = "메모를 뭐라고 메모하지?!")
        String memo,
        @Schema(description = "주문 날짜, 꼭 예시와 같은 포멧으로 보내주세요", example = "yyyy-mm-dd")
        @NotNull(message = "주문 날짜를 입력해주세요")
        LocalDate date,
        @Schema(description = "주문 수량", example = "100")
        @NotNull(message = "주문 수량을 입력해주세요") @Min(value = 1, message = "주문 수량은 1개 이상이여야 합니다.")
        Integer orderAmount
){
}
