package com.yellobook.domain.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TeamResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTeamResponseDTO {
        @Schema(description = "생성된 팀의 고유 id", example ="123")
        private Long teamId;
        @Schema(description = "팀이 생성된 시간", example ="2024-07-20T12:34:56")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteTeamResponseDTO {
        @Schema(description = "팀 초대 링크 URL", example = "https://~")
        private String inviteUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveTeamResponseDTO {
        @Schema(description = "나간 팀의 고유 id", example = "1234")
        private Long teamId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinTeamResponseDTO {
        @Schema(description = "합류한 팀의 고유 id", example = "12345")
        private Long teamId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTeamResponseDTO {
        @Schema(description = "가져오는 팀의 고유 id",example = "123")
        private Long teamId;
        @Schema(description = "가져오는 팀의 이름", example ="나이키")
        private String name;
        @Schema(description = "가져오는 팀의 전화번호", example ="012345678")
        private String phone;
        @Schema(description = "가져오는 팀의 주소", example ="서울특별시 강남구")
        private String address;
    }
}
