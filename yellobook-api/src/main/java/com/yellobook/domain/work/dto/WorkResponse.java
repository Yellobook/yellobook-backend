package com.yellobook.domain.work.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class WorkResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostWorkResponseDTO {
        private Long workId;
        private LocalDateTime createdAt;
    }
}
