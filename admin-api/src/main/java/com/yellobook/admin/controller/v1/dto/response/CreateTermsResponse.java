package com.yellobook.admin.controller.v1.dto.response;

public record CreateTermsResponse(
        long termsId
) {
    public static CreateTermsResponse of(Long termsId) {
        return new CreateTermsResponse(termsId);
    }
}
