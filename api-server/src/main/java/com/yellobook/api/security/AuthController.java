package com.yellobook.api.security;

import com.yellobook.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "AUTHAPI", description = "Auth Endpoints")
public class AuthController {
    private final JwtService jwtService;
    private final CookieProperties properties;

    public AuthController(JwtService jwtService, CookieProperties properties) {
        this.jwtService = jwtService;
        this.properties = properties;
    }

    @Operation(summary = "accessToken 재발급")
    @PostMapping("/token/reissue")
    public ApiResponse<AccessTokenRefreshResponse> reissueAccessToken(
            HttpServletRequest httpRequest
    ) {
        String refreshToken = JwtHttpExtractor.extractRefreshToken(httpRequest, properties.refresh()
                .name());
        var result = jwtService.reissueAccessToken(refreshToken);
        return ApiResponse.success(AccessTokenRefreshResponse.of(result));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<?> logout(
            HttpServletRequest httpRequest
    ) {
        String accessToken = JwtHttpExtractor.extractAccessToken(httpRequest);
        String refreshToken = JwtHttpExtractor.extractRefreshToken(httpRequest, properties.refresh()
                .name());
        jwtService.invalidateTokens(accessToken, refreshToken);
        return ApiResponse.success();
    }
}
