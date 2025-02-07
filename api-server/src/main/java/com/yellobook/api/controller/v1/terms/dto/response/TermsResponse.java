package com.yellobook.api.controller.v1.terms.dto.response;

import com.yellobook.core.domain.terms.Terms;
import java.util.List;

public record TermsResponse(
        long termsId,
        String name,
        List<TermsItemResponse> termsItems
) {
    public record TermsItemResponse(
            long termsItemId,
            String title,
            String content,
            String termsType
    ) {
    }

    public static TermsResponse of(Terms terms) {
        return new TermsResponse(
                terms.termsId(),
                terms.name(),
                terms.termsItems()
                        .stream()
                        .map(termsItem -> new TermsItemResponse(
                                termsItem.termsItemId(),
                                termsItem.title(),
                                termsItem.content(),
                                termsItem.termsType()
                                        .getDisplayName()
                        ))
                        .toList()
        );
    }
}
