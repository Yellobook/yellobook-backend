package com.yellobook.api.controller.v1.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ApproveRejectRequest(
        @NotBlank(message = "수락, 거절 여부는 필수 입력 사항입니다.")
        @Schema(description = "수락 : true, 거절 : false", example = "true")
        Boolean approve
) {
}
