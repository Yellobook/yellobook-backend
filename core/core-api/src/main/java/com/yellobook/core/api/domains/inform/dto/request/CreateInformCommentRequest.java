package com.yellobook.core.api.domains.inform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CreateInformCommentRequest(
        @Schema(description = "작성한 댓글", example = "확인했습니다.")
        String content
) {
}
