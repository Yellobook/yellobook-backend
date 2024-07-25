package com.yellobook.domain.work.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class WorkResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateWorkResponseDTO {
        private Long workId;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveWorkResponseDTO {
        private Long workId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetWorkResponseDTO {
        private String title;
        private String memo;
        private int view;
        private List<WorkCommentResponse.CommentResponseDTO> comments;
        private Date date;
    }


}
