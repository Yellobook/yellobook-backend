package com.yellobook.domain.auth.controller;

import com.yellobook.domain.auth.service.AuthService;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = " \uD83E\uDDD1\uD83C\uDFFC\u200D\uD83E\uDDB1 인증 ", description = "Authentication API")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<String>> logout(){
        //authService.logout();
        return null;
    }

    @Operation(summary = "회원 탈퇴")
    @PatchMapping("/deactivate")
    public ResponseEntity<SuccessResponse<String>> deactivate(){
        //authService.deactivate();
        return null;
    }

    @Operation(summary = "약관 동의")
    @PatchMapping("/terms")
    public ResponseEntity<SuccessResponse<String>> agreeTerm(){
        //authService.agreeTerm();
        return null;
    }
}
