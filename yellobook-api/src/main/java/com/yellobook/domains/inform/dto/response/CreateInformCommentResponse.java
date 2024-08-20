package com.yellobook.domains.inform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateInformCommentResponse(
        @Schema(description = "작성한 댓글의 고유 id")
        Long id,
        @Schema(description = "작성한 댓글의 생성 시간")
        LocalDateTime createdAt
) {
}
