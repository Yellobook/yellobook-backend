package com.yellobook.domains.inform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

public class InformRequest {
    @Getter
    @AllArgsConstructor
    public static class CreateInformRequestDTO {
        @Schema(description = "작성할 글의 제목")
        private String title;
        @Schema(description = "작성할 글의 내용")
        private String memo;
        @Schema(description = "함께 하는 멤버")
        private MentionDTO mentioned;
        @Schema(description = "날짜")
        private Date date;
    }
    public static class MentionRequestDTO {
        @Schema(description = "멘션하고자 하는 멤버")
        private Long id;
    }
}
