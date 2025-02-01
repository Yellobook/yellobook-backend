package com.yellobook.core.domain.terms;

import org.springframework.stereotype.Component;

@Component
public class AgreementManager {
    private final TermsRepository termsRepository;

    public AgreementManager(TermsRepository termsRepository) {
        this.termsRepository = termsRepository;
    }

    boolean hasMemberAgreedToActiveTerms(Long memberId) {
        return termsRepository.hasMemberAgreedToActiveTerms(memberId);
    }

    void agree(TermsAgreement termsAgreement) {
        termsRepository.agree(termsAgreement);
    }
}
