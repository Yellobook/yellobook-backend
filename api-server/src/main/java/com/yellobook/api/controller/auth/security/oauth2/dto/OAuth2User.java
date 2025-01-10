package com.yellobook.api.controller.auth.security.oauth2.dto;

import com.yellobook.api.controller.auth.enums.MemberRole;
import com.yellobook.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2User {
    private final Long memberId;
    private final String nickname;
    private final String email;
    private final String profileImage;
    private final Boolean allowance;
    private final MemberRole role;


    @Builder
    private OAuth2User(Long memberId, String nickname, String email, String profileImage, Boolean allowance,
                       MemberRole role) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.allowance = allowance;
        this.role = role;
    }

    public static OAuth2User from(Member member) {
        return OAuth2User.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .allowance(member.getAllowance())
                .role(member.getRole())
                .build();
    }
}
