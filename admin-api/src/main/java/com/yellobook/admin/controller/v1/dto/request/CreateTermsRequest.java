package com.yellobook.admin.controller.v1.dto.request;

import static com.yellobook.admin.domain.NewTerms.NewTermsItem;

import com.yellobook.admin.domain.NewTerms;
import com.yellobook.core.enums.TermsType;
import java.util.List;

public record CreateTermsRequest(
        String name,
        List<TermsItemRequest> termsItems
) {
    public record TermsItemRequest(
            String title,
            String content,
            TermsType termsType
    ) {
    }

    public NewTerms toNewTerms() {
        return new NewTerms(
                name,
                termsItems.stream()
                        .map(termsItemRequest -> new NewTermsItem(
                                termsItemRequest.title(),
                                termsItemRequest.content(),
                                termsItemRequest.termsType())
                        )
                        .toList()
        );
    }
}
