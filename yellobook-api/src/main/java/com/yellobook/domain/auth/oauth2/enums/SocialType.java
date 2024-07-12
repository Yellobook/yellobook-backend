package com.yellobook.domain.auth.oauth2.enums;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver");

    private final String registrationId;

    SocialType(String registrationId) {
        this.registrationId = registrationId;
    }

    private String getRegistrationId() {
        return registrationId;
    }

    public static SocialType from(String registrationId) {
        for (SocialType socialType : SocialType.values()) {
            if(socialType.getRegistrationId().equals(registrationId)) {
                return socialType;
            }
        }
        throw new OAuth2AuthenticationException("OAuth2 제공자가 올바르지 않습니다. registrationId: %s".formatted(registrationId));
    }
}
