package com.yellobook.core.domain.terms;

import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class TermsService {
    private final TermsAgreementAppender termsAgreementAppender;
    private final TermsReader termsReader;
    private final TermsAgreementValidator termsAgreementValidator;

    public TermsService(TermsAgreementAppender termsAgreementAppender, TermsReader termsReader,
                        TermsAgreementValidator termsAgreementValidator) {
        this.termsAgreementAppender = termsAgreementAppender;
        this.termsReader = termsReader;
        this.termsAgreementValidator = termsAgreementValidator;
    }

    public TermsAgreement agreeToActiveTerms(Member member, NewTermsAgreement newTermsAgreement) {
        termsAgreementValidator.validateTermsAgreement(member, newTermsAgreement);
        return termsAgreementAppender.agreeToActiveTerms(member, newTermsAgreement);
    }

    public Terms getActiveTerms() {
        return termsReader.readActive();
    }

    public boolean hasMemberAgreedToActiveTerms(long memberId) {
        return termsAgreementValidator.hasMemberAgreedToActiveTerms(memberId);
    }
}
