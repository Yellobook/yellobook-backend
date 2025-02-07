package com.yellobook.admin.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "⚙\uFE0F 어드민", description = "Admin API")
public interface AdminApiDocs {
    @Operation(summary = "관리자 로그인", description = "관리자 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(example = """
                                {
                                    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                    "refreshToken": "dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg"
                                }
                            """))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (아이디 또는 비밀번호 불일치)"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
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
}

