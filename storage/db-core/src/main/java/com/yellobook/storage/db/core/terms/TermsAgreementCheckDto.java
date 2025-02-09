package com.yellobook.storage.db.core.terms;

import com.querydsl.core.annotations.QueryProjection;
import com.yellobook.core.domain.terms.TermsAgreementCheckResult;

public class TermsAgreementCheckDto {
    private final long termsId;
    private final boolean isActive;
    private final boolean hasAgreed;
    private final boolean hasAllRequiredField;

    @QueryProjection
    public TermsAgreementCheckDto(long termsId, boolean isActive, boolean hasAgreed, boolean hasAllRequiredField) {
        this.termsId = termsId;
        this.isActive = isActive;
        this.hasAgreed = hasAgreed;
        this.hasAllRequiredField = hasAllRequiredField;
    }


    public long getTermsId() {
        return termsId;
    }


    public boolean isActive() {
        return isActive;
    }

    public boolean hasAgreed() {
        return hasAgreed;
    }

    public boolean hasAllRequiredField() {
        return hasAllRequiredField;
    }

    public static TermsAgreementCheckResult toTermsAgreementCheckResult(TermsAgreementCheckDto termsAgreementCheckDto) {
        return new TermsAgreementCheckResult(
                termsAgreementCheckDto.getTermsId(),
                termsAgreementCheckDto.isActive(),
                termsAgreementCheckDto.hasAgreed(),
                termsAgreementCheckDto.hasAllRequiredField()
        );
    }
}

