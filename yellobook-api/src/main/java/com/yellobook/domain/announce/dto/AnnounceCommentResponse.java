package com.yellobook.domain.announce.dto;

import java.time.LocalDateTime;

public class AnnounceCommentResponse {
    public static class CommentResponseDTO {
        private Long id;
        private Long memberId;
        private String content;
        private LocalDateTime createdAt;
    }
}
