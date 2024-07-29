package com.yellobook.domain.auth.security.oauth2.dto;

import com.yellobook.common.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2UserDTO oauth2UserDTO;

    @Override
    public <A> A getAttribute(String name) {
        return (A) getAttributes().get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "email", oauth2UserDTO.getEmail(),
                "nickname", oauth2UserDTO.getNickname(),
                "profileImage", oauth2UserDTO.getProfileImage()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> oauth2UserDTO.getRole().getRoleName());
        return collection;
    }

    @Override
    public String getName() {
        return oauth2UserDTO.getEmail();
    }

    // getAttribute 로 가져올 수 있지만 명시적으로 가져오기 위해 추가
    public Long getMemberId() {
        return oauth2UserDTO.getMemberId();
    }
    public MemberRole getRole() {
        return oauth2UserDTO.getRole();
    }

    public String getNickname() {
        return oauth2UserDTO.getNickname();
    }

    public String getEmail() {
        return oauth2UserDTO.getEmail();
    }

    public String getProfileImage() {
        return oauth2UserDTO.getProfileImage();
    }
}
