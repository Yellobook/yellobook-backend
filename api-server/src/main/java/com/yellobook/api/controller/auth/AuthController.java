package com.yellobook.api.controller.auth;

import com.yellobook.api.controller.auth.dto.request.AccessTokenRefreshRequest;
import com.yellobook.api.controller.auth.dto.response.AccessTokenRefreshResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.auth.JwtHttpExtractor;
import com.yellobook.api.support.auth.JwtService;
import com.yellobook.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "\uD83D\uDD11 인증", description = "Auth API")
public class AuthController {
    private final JwtService jwtService;

    @Operation(summary = "accessToken 재발급")
    @PostMapping("/token/reissue")
    public ApiResponse<AccessTokenRefreshResponse> reissueAccessToken(
            AccessTokenRefreshRequest request,
            ApiMember apiMember
    ) {
        var result = jwtService.reissueAccessToken(request.refreshToken(), apiMember.appMemberRole());
        return ApiResponse.success(AccessTokenRefreshResponse.of(result));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<?> logout(
            HttpServletRequest request,
            ApiMember apiMember
    ) {
        String accessToken = JwtHttpExtractor.extractAccessTokenFromHttpHeader(request);
        jwtService.invalidateTokens(apiMember.memberId(), accessToken);
        return ApiResponse.success();
    }
}
