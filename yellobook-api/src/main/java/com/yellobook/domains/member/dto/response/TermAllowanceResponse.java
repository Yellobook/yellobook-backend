package com.yellobook.domains.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TermAllowanceResponse(
        @Schema(description = "해당 멤버의 약관 동의 여부", example = "true")
        boolean allowance
) {
}
