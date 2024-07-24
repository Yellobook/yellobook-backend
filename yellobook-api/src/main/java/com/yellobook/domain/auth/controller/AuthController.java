package com.yellobook.domain.auth.controller;

import com.yellobook.domain.auth.dto.TokenResponse;
import com.yellobook.domain.auth.service.AuthService;
import com.yellobook.domain.auth.service.CookieService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "\uD83D\uDD11 인증", description = "Auth API")
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @Operation(summary = "accessToken 재발급")
    @PostMapping("/token/reissue")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(HttpServletRequest request) {
        String refreshToken = cookieService.getRefreshToken(request);
        TokenResponse tokenResponse = authService.reissueToken(refreshToken);
        return ResponseFactory.success(tokenResponse);
    }
}

