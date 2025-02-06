package com.yellobook.core.domain.terms;

public record TermsAgreementCheckResult(
        long termsId,
        boolean isActive,
        boolean hasAgreed,
        boolean hasAllRequiredFields
) {
}
