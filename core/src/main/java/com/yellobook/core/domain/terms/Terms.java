package com.yellobook.core.domain.terms;

import java.util.List;

public record Terms(
        long termsId,
        String name,
        int version,
        List<TermsItem> termsItems
) {
    public record TermsItem(
            long termsItemId,
            String title,
            String content,
            TermsType termsType
    ) {
    }
}
