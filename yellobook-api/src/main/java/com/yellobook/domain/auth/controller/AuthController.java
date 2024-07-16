package com.yellobook.domain.auth.controller;


import com.yellobook.domain.auth.dto.TokenResponse;
import com.yellobook.domain.auth.service.AuthService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token/reissue")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(@RequestParam("token") String token) {
        TokenResponse tokenResponse = authService.reissueToken(token);
        return ResponseFactory.success(tokenResponse);
    }
}