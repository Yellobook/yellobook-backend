package com.yellobook.api.controller.v1.terms;

import com.yellobook.api.controller.v1.terms.dto.request.TermsAgreementRequest;
import com.yellobook.api.controller.v1.terms.dto.response.TermsAgreementResponse;
import com.yellobook.api.controller.v1.terms.dto.response.TermsResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.terms.TermsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/terms")
public class TermsController {
    private final TermsService termsService;

    public TermsController(TermsService termsService) {
        this.termsService = termsService;
    }

    @PostMapping("/agreements")
    public ApiResponse<TermsAgreementResponse> agreeToTerms(
            @RequestBody TermsAgreementRequest request,
            ApiMember apiMember
    ) {
        var result = termsService.agreeToActiveTerms(apiMember.toMember(), request.toNewTermAgreement());
        return ApiResponse.success(TermsAgreementResponse.of());
    }

    @GetMapping("/active")
    public ApiResponse<TermsResponse> getActiveTerms(
    ) {
        var result = termsService.getActiveTerms();
        return ApiResponse.success(TermsResponse.of());
    }
}

