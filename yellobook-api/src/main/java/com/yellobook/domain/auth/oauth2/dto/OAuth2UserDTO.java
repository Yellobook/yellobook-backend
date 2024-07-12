package com.yellobook.domain.auth.oauth2.dto;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2UserDTO {
    private final String nickname;
    private final String email;
    private final String profileImage;
    private final MemberRole role;

    @Builder
    private OAuth2UserDTO(String nickname, String email, String profileImage, MemberRole role) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    public static OAuth2UserDTO from(Member member) {
        if(member == null) {
            return OAuth2UserDTO.builder()
                    .role(MemberRole.GUEST)
                    .build();
        }
        return OAuth2UserDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .role(member.getRole())
                .build();
    }
}
