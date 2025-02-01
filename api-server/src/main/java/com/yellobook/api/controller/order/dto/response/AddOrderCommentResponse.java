package com.yellobook.api.controller.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddOrderCommentResponse(
        @Schema(description = "생성한 댓글 Id", example = "1")
        Long commentId
) {
}
