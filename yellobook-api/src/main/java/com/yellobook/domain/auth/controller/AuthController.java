package com.yellobook.domain.auth.controller;

import com.yellobook.domain.auth.dto.request.AllowanceRequest;
import com.yellobook.domain.auth.dto.request.LogoutRequest;
import com.yellobook.domain.auth.dto.response.AllowanceResponse;
import com.yellobook.domain.auth.dto.response.TokenResponse;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.service.AuthService;
import com.yellobook.domain.auth.service.CookieService;
import com.yellobook.domain.auth.service.JwtService;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(
            HttpServletRequest request
    ) {
        String refreshToken = cookieService.getRefreshToken(request);
        TokenResponse tokenResponse = authService.reissueToken(refreshToken);
        return ResponseFactory.success(tokenResponse);
    }

    @Operation(summary = "약관 동의")
    @PostMapping("/terms")
    public ResponseEntity<SuccessResponse<AllowanceResponse>> agreeToTerms(
            @Valid @RequestBody AllowanceRequest request
    ) {
        String allowanceToken = request.getToken();
        AllowanceResponse allowanceResponse = authService.updateAllowance(allowanceToken);
        return ResponseFactory.success(allowanceResponse);
    }


    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        String accessToken = request.getAccessToken();
        // 본인확인
        Long tokenMemberId = jwtService.getMemberIdFromAccessToken(accessToken);
        Long loggedInMemberId = oAuth2User.getMemberId();
        if (!tokenMemberId.equals(loggedInMemberId)) {
            throw new CustomException(AuthErrorCode.UNAUTHORIZED_ACCESS);
        }
        // 로그아웃
        authService.logout(accessToken, loggedInMemberId);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "회원 탈퇴")
    @PatchMapping("/deactivate")
    public ResponseEntity<SuccessResponse<String>> deactivate(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
//        authService.deactivate(oAuth2User);
        return null;
    }

}
