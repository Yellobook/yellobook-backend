package com.yellobook.admin.controller.v1.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "약관 생성 응답")
public record CreateTermsResponse(
        @Schema(description = "생성된 약관의 ID", example = "1")
        long termsId
) {
    public static CreateTermsResponse of(Long termsId) {
        return new CreateTermsResponse(termsId);
    }
}
