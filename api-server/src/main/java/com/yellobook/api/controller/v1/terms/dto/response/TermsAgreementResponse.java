package com.yellobook.api.controller.v1.terms.dto.response;

import com.yellobook.core.domain.terms.TermsAgreement;
import java.time.LocalDateTime;

public record TermsAgreementResponse(
        long agreementId,
        long memberId,
        long termsId,
        LocalDateTime agreeAt
) {
    public static TermsAgreementResponse of(TermsAgreement termsAgreement) {
        return new TermsAgreementResponse(
                termsAgreement.agreementId(),
                termsAgreement.memberId(),
                termsAgreement.termsId(),
                termsAgreement.agreeAt()
        );
    }
}
