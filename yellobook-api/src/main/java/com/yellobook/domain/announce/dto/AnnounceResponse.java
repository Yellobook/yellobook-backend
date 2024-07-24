package com.yellobook.domain.announce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AnnounceResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostAnnounceResponseDTO {
        private Long workId;
        private LocalDateTime createdAt;
    }
}
