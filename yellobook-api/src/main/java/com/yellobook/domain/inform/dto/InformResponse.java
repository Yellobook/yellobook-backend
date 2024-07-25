package com.yellobook.domain.inform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class InformResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateInformResponseDTO {
        private Long informId;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveInformResponseDTO {
        private Long informId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetInformResponseDTO {
        private String title;
        private String memo;
        private int view;
        private List<InformCommentResponse.CommentResponseDTO> comments;
        private Date date;
    }


}
