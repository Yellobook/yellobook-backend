package com.yellobook.domain.member.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.member.dto.MemberResponse;
import com.yellobook.domain.member.service.MemberQueryService;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "\uD83D\uDD11 사용자", description = "Member API")
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/profile")
    @Operation(summary = "멤버의 마이프로필 조회 API", description ="로그인을 한 멤버가 마이프로필을 조회하는 API입니다.")
    public ResponseEntity<SuccessResponse<MemberResponse.ProfileResponseDTO>> getMemberProfile(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        memberQueryService.findById(customOAuth2User.getMemberId());
        return null;
    }

    @GetMapping("/allowance")
    @Operation(summary = "멤버의 약관 동의 여부 조회 API", description = "로그인을 한 멤버의 약관 동의 여부를 조회하는 API입니다.")
    public ResponseEntity<SuccessResponse<MemberResponse.AllowanceResponseDTO>> getMemberAllowance(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        memberQueryService.getAllowanceById(customOAuth2User.getMemberId());
        return null;
    }
}
