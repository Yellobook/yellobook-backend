package com.yellobook.api.controller.v1.terms;

import com.yellobook.api.controller.v1.terms.dto.request.TermsAgreementRequest;
import com.yellobook.api.controller.v1.terms.dto.response.TermsAgreementResponse;
import com.yellobook.api.controller.v1.terms.dto.response.TermsResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.support.api.docs.SwaggerResponse;
import com.yellobook.support.api.docs.SwaggerResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TERMS API", description = "약관 조회 및 동의")
public interface TermsApiDocs {
    @Operation(summary = "약관 동의", description = "요청을 보낸 시점에 활성화된 약관에 동의합니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = TermsAgreementResponse.class))
                            }
                    )
            ),
            @SwaggerResponse(responseCode = "404", description = "MEMBER01"),
            @SwaggerResponse(responseCode = "404", description = "TERMS01"),
            @SwaggerResponse(responseCode = "400", description = "TERMS03"),
            @SwaggerResponse(responseCode = "400", description = "TERMS04")
    })
    ApiResponse<TermsAgreementResponse> agreeToTerms(TermsAgreementRequest request,
                                                     @Parameter(hidden = true) ApiMember member);


    @Operation(summary = "약관 조회", description = "요청을 보낸 시점에 활성화된 약관을 조회합니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = TermsResponse.class))
                            }
                    )
            ),
            @SwaggerResponse(responseCode = "404", description = "MEMBER01"),
            @SwaggerResponse(responseCode = "404", description = "TERMS02")

    })
    ApiResponse<TermsResponse> getActiveTerms();
}
