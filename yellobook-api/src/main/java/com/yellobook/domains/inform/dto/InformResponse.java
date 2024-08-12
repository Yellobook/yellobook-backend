package com.yellobook.domains.inform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "가지고 오는 글의 제목")
        private String title;
        @Schema(description = "가지고 오는 글에 있는 메모")
        private String memo;
        @Schema(description = "함께 하는 멤버")
        private MentionDTO mentioned;
        @Schema(description = "조회수")
        private int view;
        @Schema(description = "댓글")
        private List<InformCommentResponse.CommentResponseDTO> comments;
        @Schema(description = "날짜")
        private Date date;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MentionResponseDTO {
        @Schema(description = "멘션된 멤버(들)")
        private MentionDTO mentioned;
    }

}
