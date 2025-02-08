package com.yellobook.api.controller.v1.terms;

import com.yellobook.api.controller.v1.terms.dto.request.TermsAgreementRequest;
import com.yellobook.api.controller.v1.terms.dto.response.TermsAgreementResponse;
import com.yellobook.api.controller.v1.terms.dto.response.TermsResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "\uD83D\uDCC3 이용 약관", description = "Terms Endpoints")
public interface TermsApiDocs {

    @Operation(summary = "약관 동의", description = "활성화된 약관을 동의하는 엔드포인트입니다.")
    ApiResponse<TermsAgreementResponse> agreeToTerms(TermsAgreementRequest request,
                                                     @Parameter(hidden = true) ApiMember member);

    @Operation(summary = "약관 조회", description = "활성화된 약관을 조회하는 엔드포인트입니다.")
    ApiResponse<TermsResponse> getActiveTerms();
}
