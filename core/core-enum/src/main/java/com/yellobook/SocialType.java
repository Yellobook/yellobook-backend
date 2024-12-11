package com.yellobook;

import java.util.NoSuchElementException;
import lombok.Getter;

@Getter
public enum SocialType {
    KAKAO("kakao", "카카오 로그인"),
    NAVER("naver", "네이버 로그인");

    private final String registrationId;
    private final String title;

    SocialType(String registrationId, String title) {
        this.registrationId = registrationId;
        this.title = title;
    }

    public static SocialType from(String registrationId) {
        for (SocialType socialType : SocialType.values()) {
            if (socialType.getRegistrationId()
                    .equals(registrationId)) {
                return socialType;
            }
        }
        throw new NoSuchElementException("OAuth2 제공자가 올바르지 않습니다. registrationId: %s".formatted(registrationId));
    }
}
