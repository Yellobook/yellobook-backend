package com.yellobook.core.domain.terms;

import org.springframework.stereotype.Service;

@Service
public class TermsService {
    private final AgreementManager agreementManager;

    public TermsService(AgreementManager agreementManager) {
        this.agreementManager = agreementManager;
    }

    public boolean hasMemberAgreedToActiveTerms(Long memberId) {
        return agreementManager.hasMemberAgreedToActiveTerms(memberId);
    }
}
