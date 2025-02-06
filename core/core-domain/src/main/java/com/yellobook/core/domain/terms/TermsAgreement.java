package com.yellobook.core.domain.terms;

import java.time.LocalDateTime;

public record TermsAgreement(
        long agreementId,
        long memberId,
        long termsId,
        LocalDateTime agreeAt
) {
}
