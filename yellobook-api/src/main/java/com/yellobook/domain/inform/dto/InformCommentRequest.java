package com.yellobook.domain.inform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class InformCommentRequest {
    @Getter
    @AllArgsConstructor
    public static class PostCommentRequestDTO {
        @Schema(description = "작성한 댓글")
        private String content;
    }
}
