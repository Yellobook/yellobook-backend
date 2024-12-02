package com.yellobook.core.api.domains.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateTeamResponse(
        @Schema(description = "생성된 팀의 고유 id", example = "123")
        Long teamId,
        @Schema(description = "팀이 생성된 시간", example = "2024-07-20T12:34:56")
        LocalDateTime createdAt
) {
}
