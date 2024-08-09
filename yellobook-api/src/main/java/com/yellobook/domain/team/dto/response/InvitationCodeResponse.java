package com.yellobook.domain.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record InvitationCodeResponse(
        @Schema(description = "팀 초대 링크 URL", example = "https://~")
        String inviteUrl
) {
}
