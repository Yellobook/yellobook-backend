package com.yellobook.common.controller;

import com.yellobook.domains.auth.service.AuthService;
import com.yellobook.domains.auth.service.CookieService;
import com.yellobook.domains.auth.service.JwtService;
import com.yellobook.error.code.CommonErrorCode;
import com.yellobook.error.exception.CustomException;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile({"dev", "local"})
@RequestMapping("/api/v1/dev")
@Tag(name = "\uD83D\uDC68\u200D\uD83D\uDCBB 개발용", description = "Dev Token API")
public class DevController {
    private final AuthService authService;
    private final CookieService cookieService;
    private final JwtService jwtService;

    @Operation(summary = "accessToken, refreshToken 발급", description = "개발시 ADMIN, ORDERER, VIEWER 의 토큰을 각각 발급받을 수 있는 API 입니다.")
    @PostMapping("/token")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(
            @RequestBody TokenRequest request
    ) {
        // data.sql 로 넣은 임시 사용자,
        Long memberId = switch (request.role()) {
            case "ADMIN":
                yield 1L;
            case "ORDERER":
                yield 2L;
            case "VIEWER":
                yield 3L;
            default:
                throw new CustomException(CommonErrorCode.BAD_REQUEST);
        };
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        log.info("[DEV_INFO] 테스트용 임시 사용자의 accessToken, refreshToken 발급");
        var result = new TokenResponse(accessToken, refreshToken);
        return ResponseFactory.success(result);
    }

    public record TokenRequest(
            @Schema(description = "팀에서 사용자의 권한", example = "ADMIN")
            String role
    ) {
    }

    public record TokenResponse(
            String accessToken,
            String refreshToken
    ) {
    }
}
