package com.yellobook.domain.work.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class WorkCommentResponse {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentResponseDTO {
        @Schema(description = "작성한 댓글의 고유 id")
        private Long id;
        @Schema(description = "댓글을 작성한 멤버의 id")
        private Long memberId;
        @Schema(description = "작성한 댓글의 내용")
        private String content;
        @Schema(description = "작성한 댓글의 생성 시간")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostCommentResponseDTO {
        @Schema(description = "작성한 댓글의 고유 id")
        private Long id;
        @Schema(description = "작성한 댓글의 생성 시간")
        private LocalDateTime createdAt;
    }
}
