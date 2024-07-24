package com.yellobook.domain.announce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class AnnounceResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostAnnounceResponseDTO {
        private Long AnnounceId;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveAnnounceResponseDTO {
        private Long AnnounceId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAnnounceResponseDTO {
        private String title;
        private String memo;
        private int view;
        private List<AnnounceCommentResponse.CommentResponseDTO> comments;
        private Date date;
    }
}
