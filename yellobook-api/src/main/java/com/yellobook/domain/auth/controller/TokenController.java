package com.yellobook.domain.auth.controller;

import com.yellobook.domain.auth.dto.TokenResponse;
import com.yellobook.domain.auth.service.AuthService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "\uD83D\uDD11 토큰", description = "Token API")
@RestController(value = "/api/v1/token")
@RequiredArgsConstructor
public class TokenController {
    private final AuthService authService;

    @Operation(summary = "accessToken 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(@RequestParam("token") String token) {
        TokenResponse tokenResponse = authService.reissueToken(token);
        return ResponseFactory.success(tokenResponse);
    }
}