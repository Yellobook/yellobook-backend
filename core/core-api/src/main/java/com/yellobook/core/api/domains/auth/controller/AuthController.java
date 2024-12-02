package com.yellobook.core.api.domains.auth.controller;

import com.yellobook.core.api.domains.auth.dto.request.AllowanceRequest;
import com.yellobook.core.api.domains.auth.dto.response.AllowanceResponse;
import com.yellobook.core.api.domains.auth.dto.response.RefreshResponse;
import com.yellobook.core.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.core.api.domains.auth.service.AuthService;
import com.yellobook.core.api.domains.auth.service.CookieService;
import com.yellobook.core.api.domains.auth.service.JwtService;
import com.yellobook.core.api.support.response.ResponseFactory;
import com.yellobook.core.api.support.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "\uD83D\uDD11 인증", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;
    private final JwtService jwtService;

    @Operation(summary = "accessToken 재발급")
    @PostMapping("/token/reissue")
    public ResponseEntity<SuccessResponse<RefreshResponse>> reissueToken(
            HttpServletRequest request
    ) {
        String refreshToken = cookieService.getRefreshToken(request);
        var result = authService.reissueToken(refreshToken);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "약관 동의")
    @PostMapping("/terms")
    public ResponseEntity<SuccessResponse<AllowanceResponse>> agreeToTerms(
            @Valid @RequestBody AllowanceRequest request,
            HttpServletResponse response
    ) throws IOException {
        var result = authService.updateAllowance(request.token());
        return ResponseFactory.success(result);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        String accessToken = jwtService.resolveAccessToken(request);
        authService.logout(oAuth2User.getMemberId(), accessToken);
        return ResponseFactory.noContent();
    }
}
