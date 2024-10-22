package com.yellobook.domains.auth.security.oauth2.dto;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2UserDTO {
    private final Long memberId;
    private final String nickname;
    private final String email;
    private final String profileImage;
    private final Boolean allowance;
    private final MemberRole role;


    @Builder
    private OAuth2UserDTO(Long memberId, String nickname, String email, String profileImage, Boolean allowance,
                          MemberRole role) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.allowance = allowance;
        this.role = role;
    }

    public static OAuth2UserDTO from(Member member) {
        return OAuth2UserDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .allowance(member.getAllowance())
                .role(member.getRole())
                .build();
    }
}
