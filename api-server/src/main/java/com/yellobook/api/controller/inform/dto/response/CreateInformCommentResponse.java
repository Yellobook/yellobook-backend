package com.yellobook.api.controller.inform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record CreateInformCommentResponse(
        @Schema(description = "작성한 댓글의 고유 id")
        Long id,
        @Schema(description = "작성한 댓글의 생성 시간")
        LocalDateTime createdAt
) {
}
