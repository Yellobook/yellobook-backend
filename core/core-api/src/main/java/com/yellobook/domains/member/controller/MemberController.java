package com.yellobook.domains.member.controller;


import com.yellobook.common.resolver.annotation.TeamMember;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.member.dto.response.CurrentTeamResponse;
import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.service.MemberQueryService;
import com.yellobook.support.response.ResponseFactory;
import com.yellobook.support.response.SuccessResponse;
import com.yellobook.common.vo.TeamMemberVO;
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
    @Operation(summary = "마이프로필 조회", description = "로그인한 사용자의 마이프로필 조회 API")
    public ResponseEntity<SuccessResponse<ProfileResponse>> getMemberProfile(
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        var result = memberQueryService.getMemberProfile(user.getMemberId());
        return ResponseFactory.success(result);
    }

    @GetMapping("/teams/current")
    @Operation(summary = "사용자가 현재 위치한 팀 조회", description = "로그인한 사용자가 현재 위치한 팀 정보를 반환하는 API")
    public ResponseEntity<SuccessResponse<CurrentTeamResponse>> getMemberCurrentTeam(
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = memberQueryService.getMemberCurrentTeam(teamMember.getTeamId());
        return ResponseFactory.success(result);
    }

//    @PatchMapping("/deactivate")
//    @Operation(summary = "회원 탈퇴", description = "회원탈퇴 API \n 30일간 비활성화 후 데이터 삭제")
//    public ResponseEntity<Void> deactivate(
//            @AuthenticationPrincipal CustomOAuth2User user
//    ) {
//        memberCommandService.deactivate(user.getMemberId());
//        return ResponseFactory.noContent();
//    }
}
