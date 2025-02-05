package com.yellobook.api.controller.inform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateInformCommentRequest(
        @Schema(description = "작성한 댓글", example = "확인했습니다.")
        String content
) {
}
