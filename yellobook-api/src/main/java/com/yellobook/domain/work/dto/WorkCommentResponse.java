package com.yellobook.domain.work.dto;

import java.time.LocalDateTime;

public class WorkCommentResponse {
    public static class CommentResponseDTO {
        private Long id;
        private Long memberId;
        private String content;
        private LocalDateTime createdAt;
    }
}
