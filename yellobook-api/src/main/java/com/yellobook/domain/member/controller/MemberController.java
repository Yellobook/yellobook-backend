package com.yellobook.domain.member.controller;

import com.yellobook.common.annotation.TeamMember;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.member.dto.response.ProfileResponse;
import com.yellobook.domain.member.service.MemberQueryService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "마이프로필 조회", description ="로그인을 한 멤버가 마이프로필을 조회하는 API입니다.")
    public ResponseEntity<SuccessResponse<ProfileResponse>> getMemberProfile(
            @TeamMember TeamMemberVO teamMember
    ){
        ProfileResponse response = memberQueryService.getMemberProfile(teamMember.getMemberId());
        return ResponseFactory.success(response);
    }
}
