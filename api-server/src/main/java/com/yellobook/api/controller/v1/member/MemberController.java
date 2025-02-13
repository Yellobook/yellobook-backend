package com.yellobook.api.controller.v1.member;

import com.yellobook.api.controller.member.dto.request.UpdateBioRequest;
import com.yellobook.api.controller.member.dto.request.UpdateNicknameRequest;
import com.yellobook.api.controller.member.dto.response.JoinedTeamsResponse;
import com.yellobook.api.controller.member.dto.response.ProfileResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.member.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController implements MemberApiDocs {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/my/profile")
    public ApiResponse<ProfileResponse> getMemberProfile(
            ApiMember apiMember
    ) {
        return ApiResponse.success(ProfileResponse.of(apiMember));
    }


    @PatchMapping("/my/profile/nickname")
    public ApiResponse<?> updateNickname(
            ApiMember apiMember,
            @RequestBody UpdateNicknameRequest request
    ) {
        memberService.updateNickname(apiMember.toMember(), request.nickname());
        return ApiResponse.success();
    }

    @PatchMapping("/my/profile/bio")
    public ApiResponse<?> updateBio(
            ApiMember apiMember,
            @RequestBody UpdateBioRequest request
    ) {
        memberService.updateBio(apiMember.toMember(), request.newBio());
        return ApiResponse.success();
    }

    @GetMapping("/my/stores")
    public ApiResponse<JoinedTeamsResponse> getMemberJoinedTeams(
            ApiMember apiMember
    ) {
        var result = memberService.getMemberJoinedStores(apiMember.toMember());
        return ApiResponse.success(JoinedTeamsResponse.of(apiMember.toMember(), result));
    }
}