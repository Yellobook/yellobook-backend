package com.yellobook.api.security;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2 로그인", description = "Spring Security의 로그인 경로")
@Hidden // 실제 동작하는 컨트롤러가 아님을 표시 (Swagger 전용)
@RestController
@RequestMapping("/oauth2/authorization")
public class SwaggerController {

    @Operation(summary = "카카오 로그인", description = "카카오 OAuth2 로그인을 시작합니다.")
    @ApiResponse(responseCode = "302", description = "로그인 후 리다이렉트됨")
    @GetMapping("/kakao")
    public void kakaoLogin() {
        throw new UnsupportedOperationException("Spring Security에서 자동 처리됩니다.");
    }

    @Operation(summary = "네이버 로그인", description = "네이버 OAuth2 로그인을 시작합니다.")
    @ApiResponse(responseCode = "302", description = "로그인 후 리다이렉트됨")
    @GetMapping("/naver")
    public void naverLogin() {
        throw new UnsupportedOperationException("Spring Security에서 자동 처리됩니다.");
    }
}

