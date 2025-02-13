package com.yellobook.api.config;

public enum DocsSocialType {
    NAVER("naver", "네이버 로그인"),
    KAKAO("kakao", "카카오 로그인");

    private final String provider;
    private final String displayName;

    DocsSocialType(String provider, String displayName) {
        this.provider = provider;
        this.displayName = displayName;
    }

    public String getProvider() {
        return provider;
    }

    public String getDisplayName() {
        return displayName;
    }

}
