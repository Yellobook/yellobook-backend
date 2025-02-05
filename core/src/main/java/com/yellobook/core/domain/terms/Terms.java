package com.yellobook.core.domain.terms;

import java.util.List;

public record Terms(
        Long termsId,
        String name,
        int version,
        List<TermsItem> termsItems
) {
}
