package com.yellobook.api.controller.member.controller;


import com.yellobook.api.controller.member.dto.response.CurrentTeamResponse;
import com.yellobook.api.controller.member.dto.response.ProfileResponse;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.api.controller.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.member.service.MemberQueryService;
import com.yellobook.api.support.TeamMember;
import com.yellobook.support.response.ResponseFactory;
import com.yellobook.support.response.SuccessResponse;
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
@Tag(name = "\uD83D\uDC68\u200D\uD83C\uDF3E 사용자", description = "Member API")
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/profile")
    @Operation(summary = "마이프로필 조회", description = "사용자의 마이프로필 조회 API")
    public ResponseEntity<SuccessResponse<ProfileResponse>> getMemberProfile(
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        var result = memberQueryService.getMemberProfile(user.getMemberId());
        return ResponseFactory.success(result);
    }

    @GetMapping("/teams")
    @Operation(summary = "소속된 팀 목록 조회", description = "사용자가 소속된 팀 목록 조회 API")
    public ResponseEntity<SuccessResponse<ProfileResponse>> getMemberProfile(
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        var result = memberQueryService.getMemberProfile(user.getMemberId());
        return ResponseFactory.success(result);
    }

    @GetMapping("/teams/current")
    @Operation(summary = "사용자가 현재 위치한 팀 조회", description = "사용자가 현재 위치한 팀 정보를 반환하는 API")
    public ResponseEntity<SuccessResponse<CurrentTeamResponse>> getMemberCurrentTeam(
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = memberQueryService.getMemberCurrentTeam(teamMember.getTeamId());
        return ResponseFactory.success(result);
    }
}
