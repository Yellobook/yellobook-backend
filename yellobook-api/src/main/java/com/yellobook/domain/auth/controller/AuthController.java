package com.yellobook.domain.auth.controller;

import com.yellobook.domain.auth.dto.request.AllowanceRequest;
import com.yellobook.domain.auth.dto.request.LogoutRequest;
import com.yellobook.domain.auth.dto.response.AllowanceResponse;
import com.yellobook.domain.auth.dto.response.TokenResponse;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.service.AuthService;
import com.yellobook.domain.auth.service.CookieService;
import com.yellobook.domain.auth.service.JwtService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(
            HttpServletRequest request
    ) {
        String refreshToken = cookieService.getRefreshToken(request);
        var result = authService.reissueToken(refreshToken);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "약관 동의")
    @PostMapping("/terms")
    public ResponseEntity<SuccessResponse<AllowanceResponse>> agreeToTerms(
            @Valid @RequestBody AllowanceRequest request
    ) {
        var result = authService.updateAllowance(request.getToken());
        return ResponseFactory.success(result);
    }


    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        authService.logout(request.getAccessToken(), oAuth2User.getMemberId());
        return ResponseFactory.noContent();
    }

    @Operation(summary = "회원 탈퇴")
    @PatchMapping("/deactivate")
    public ResponseEntity<SuccessResponse<String>> deactivate(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
//        authService.deactivate(oAuth2User);
        return null;
    }

}
