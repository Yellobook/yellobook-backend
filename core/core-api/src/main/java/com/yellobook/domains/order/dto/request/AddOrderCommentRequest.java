package com.yellobook.domains.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AddOrderCommentRequest(
        @NotBlank
        @Schema(description = "추가할 댓글 내용", example = "주문수량 20개로 변경 가능할까요?")
        String content
) {
}