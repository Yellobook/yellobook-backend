package com.yellobook.admin.domain;

import com.yellobook.core.enums.TermsType;
import java.util.List;

public record NewTerms(
        String name,
        List<NewTermsItem> termsItems
) {
    public record NewTermsItem(
            String title,
            String content,
            TermsType termsType
    ) {
    }
}