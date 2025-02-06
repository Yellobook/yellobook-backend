package com.yellobook.core.domain.terms;

import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class TermsAgreementAppender {

    private final TermsAgreementRepository termsAgreementRepository;

    public TermsAgreementAppender(TermsAgreementRepository termsAgreementRepository) {
        this.termsAgreementRepository = termsAgreementRepository;
    }

    public TermsAgreement agreeToActiveTerms(Member member, NewTermsAgreement newTermsAgreement) {
        return termsAgreementRepository.agreeToActiveTerms(member.memberId(), newTermsAgreement.termsId(),
                newTermsAgreement.termsItemIds());
    }
}
