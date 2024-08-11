package com.yellobook.domains.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateTeamResponse(
        @Schema(description = "생성된 팀의 고유 id", example ="123")
        Long teamId,
        @Schema(description = "팀이 생성된 시간", example ="2024-07-20T12:34:56")
        LocalDateTime createdAt
) {
}
