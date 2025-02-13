package com.yellobook.admin.controller.v1.dto.request;

import static com.yellobook.admin.domain.NewTerms.NewTermsItem;

import com.yellobook.admin.domain.NewTerms;
import com.yellobook.core.enums.TermsType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "약관 생성 요청")
public record CreateTermsRequest(
        @Schema(description = "약관명", example = "이용 약관")
        String name,

        @Schema(description = "약관 항목 리스트")
        List<TermsItemRequest> termsItems
) {
    @Schema(description = "약관 항목 요청")
    public record TermsItemRequest(
            @Schema(description = "약관 항목 제목", example = "개인정보 보호 정책")
            String title,

            @Schema(description = "약관 항목 내용", example = "이용자의 개인정보는 보호됩니다.")
            String content,

            @Schema(description = "약관 유형", example = "MANDATORY")
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
