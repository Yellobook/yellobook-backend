package com.yellobook.core.domain.terms;

import java.util.List;

public record NewTermsAgreement(
        long termsId,
        List<Long> termsItemIds
) {
}
