package com.yellobook.core.domain.terms;

import java.util.List;
import java.util.Optional;

public interface TermsAgreementRepository {
    boolean hasMemberAgreedToActiveTerms(long memberId);

    TermsAgreement agreeToActiveTerms(long memberId, long termsId,
                                      List<Long> termsItemIds);

    Optional<TermsAgreementCheckResult> findAgreementCheckInfo(long memberId, long termsId,
                                                               List<Long> termsItemIds);
}
