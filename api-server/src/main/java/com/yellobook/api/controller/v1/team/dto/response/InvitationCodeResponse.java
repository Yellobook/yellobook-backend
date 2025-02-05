package com.yellobook.api.controller.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record InvitationCodeResponse(
        @Schema(description = "팀 초대 링크 URL", example = "https://~")
        String inviteUrl
) {
}
