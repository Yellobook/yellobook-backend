package com.yellobook.api.controller.v1.terms;

import com.yellobook.api.controller.v1.terms.dto.request.TermsAgreementRequest;
import com.yellobook.api.controller.v1.terms.dto.response.TermsAgreementResponse;
import com.yellobook.api.controller.v1.terms.dto.response.TermsResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TERMS API", description = "Terms Endpoints")
public interface TermsApiDocs {
    @Operation(summary = "약관 동의", description = "요청을 보낸 시점에 활성화된 약관에 동의합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = TermsAgreementResponse.class))
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER01"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "TERMS01"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "TERMS03"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "TERMS04")
    })
    ApiResponse<TermsAgreementResponse> agreeToTerms(TermsAgreementRequest request,
                                                     @Parameter(hidden = true) ApiMember member);


    @Operation(summary = "약관 조회", description = "요청을 보낸 시점에 활성화된 약관을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = TermsResponse.class))
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER01"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "TERMS02")

    })
    ApiResponse<TermsResponse> getActiveTerms();
}
