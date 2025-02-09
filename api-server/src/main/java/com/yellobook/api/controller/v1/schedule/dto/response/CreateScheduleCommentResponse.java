package com.yellobook.api.controller.v1.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateScheduleCommentResponse(
        @Schema(description = "작성한 댓글의 고유 id")
        Long commentId
) {
}
