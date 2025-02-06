package com.yellobook.core.domain.terms;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TermsAgreementValidator {
    private final TermsAgreementRepository termsAgreementRepository;

    public TermsAgreementValidator(TermsAgreementRepository termsAgreementRepository) {
        this.termsAgreementRepository = termsAgreementRepository;
    }

    public boolean hasMemberAgreedToActiveTerms(long memberId) {
        return termsAgreementRepository.hasMemberAgreedToActiveTerms(memberId);
    }

    public void validateTermsAgreement(Member member, NewTermsAgreement newTermsAgreement) {
        TermsAgreementCheckResult checkResult = termsAgreementRepository.findAgreementCheckInfo(
                        member.memberId(), newTermsAgreement.termsId(), newTermsAgreement.termsItemIds())
                .orElseThrow(() -> new CoreException(CoreErrorType.TERMS_NOT_FOUND));
        if (!checkResult.isActive()) {
            throw new CoreException(CoreErrorType.ACTIVE_TERMS_NOT_FOUND);
        }
        if (!checkResult.hasAgreed()) {
            throw new CoreException(CoreErrorType.TERMS_ALREADY_AGREED);
        }
        if (!checkResult.hasAllRequiredFields()) {
            throw new CoreException(CoreErrorType.REQUIRED_TERMS_NOT_AGREED);
        }
    }
}
