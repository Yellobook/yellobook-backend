package com.yellobook.api.controller.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateNicknameRequest(
        @Schema(description = "수정할 닉네임 이름", example = "옐로벅")
        String nickname
) {
}
