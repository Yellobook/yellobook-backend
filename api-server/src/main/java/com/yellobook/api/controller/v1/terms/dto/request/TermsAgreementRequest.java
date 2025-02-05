package com.yellobook.api.controller.v1.terms.dto.request;

import com.yellobook.core.domain.terms.NewTermsAgreement;
import java.util.List;

public record TermsAgreementRequest(
        long termsId,
        List<Long> termsItemIds
) {
    public NewTermsAgreement toNewTermAgreement() {
        return new NewTermsAgreement(
                termsId,
                termsItemIds
        );
    }
}
