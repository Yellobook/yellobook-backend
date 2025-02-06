package com.yellobook.api.controller.member.controller;


import com.yellobook.api.controller.member.dto.request.UpdateBioRequest;
import com.yellobook.api.controller.member.dto.request.UpdateNicknameRequest;
import com.yellobook.api.controller.member.dto.response.JoinedTeamsResponse;
import com.yellobook.api.controller.member.dto.response.ProfileResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "\uD83D\uDC68\u200D\uD83C\uDF3E 사용자", description = "Member API")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/my/profile")
    @Operation(summary = "마이프로필 조회", description = "사용자의 마이프로필 조회 API")
    public ApiResponse<ProfileResponse> getMemberProfile(
            ApiMember apiMember
    ) {
        return ApiResponse.success(ProfileResponse.of(apiMember));
    }

    @PatchMapping("/my/profile/nickname")
    @Operation(summary = "닉네임 변경", description = "사용자의 닉네임 변경 API")
    public ApiResponse<?> updateNickname(
            ApiMember apiMember,
            @RequestBody UpdateNicknameRequest request
    ) {
        memberService.updateNickname(apiMember.toMember(), request.nickname());
        return ApiResponse.success();
    }

    @PatchMapping("/my/profile/bio")
    @Operation(summary = "한줄 소개 변경", description = "사용자의 한줄소개 변경 API")
    public ApiResponse<?> updateBio(
            ApiMember apiMember,
            @RequestBody UpdateBioRequest request
    ) {
        memberService.updateBio(apiMember.toMember(), request.newBio());
        return ApiResponse.success();
    }

    @GetMapping("/my/teams")
    @Operation(summary = "소속된 팀 목록 조회", description = "사용자가 소속된 팀 목록 조회 API")
    public ApiResponse<JoinedTeamsResponse> getMemberJoinedTeams(
            ApiMember apiMember
    ) {
        var result = memberService.getMemberJoinedTeams(apiMember.toMember());
        return ApiResponse.success(JoinedTeamsResponse.of(apiMember.toMember(), result));
    }
}