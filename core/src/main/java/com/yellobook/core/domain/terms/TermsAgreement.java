package com.yellobook.core.domain.terms;

import java.util.List;

public record TermsAgreement(
        Long memberId,
        Long termsId,
        List<Long> termsItemIds
) {
}
