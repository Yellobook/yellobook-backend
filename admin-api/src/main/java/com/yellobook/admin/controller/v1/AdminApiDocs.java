package com.yellobook.admin.controller.v1;

import com.yellobook.admin.controller.v1.dto.request.CreateTermsRequest;
import com.yellobook.admin.controller.v1.dto.response.CreateTermsResponse;
import com.yellobook.admin.support.response.AdminResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "ADMIN API", description = "Admin Endpoints")
public interface AdminApiDocs {
    @Operation(summary = "관리자 로그인", description = "관리자 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                        {
                                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                            "refreshToken": "dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg"
                                        }
                                    """))),
            @ApiResponse(responseCode = "401", description = "ADMIN01"),
            @ApiResponse(responseCode = "403", description = "ADMIN02")
    })
    @PostMapping("/auth/login")
    default void adminLogin(@RequestBody @Parameter(description = "로그인 요청 데이터") AdminLoginRequest request) {
        throw new UnsupportedOperationException("Spring Security에서 자동 처리됩니다.");
    }

    record AdminLoginRequest(
            @Schema(example = "admin@example.com") String username,
            @Schema(example = "password1234") String password
    ) {
    }

    @Operation(summary = "마이프로필 조회", description = "사용자의 프로필 정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = CreateTermsResponse.class))
                            }
                    )
            ),
    })
    AdminResponse<CreateTermsResponse> createTerms(
            @Parameter(description = "생성할 약관 정보", required = true)
            @RequestBody CreateTermsRequest request
    );
}

