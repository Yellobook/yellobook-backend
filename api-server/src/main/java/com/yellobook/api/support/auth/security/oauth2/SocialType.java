package com.yellobook.api.support.auth.security.oauth2;

public enum SocialType {
    NAVER("naver", "네이버 로그인"),
    KAKAO("kakao", "카카오 로그인");

    private final String registrationId;
    private final String description;

    SocialType(String registrationId, String description) {
        this.registrationId = registrationId;
        this.description = description;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getDescription() {
        return description;
    }
}
