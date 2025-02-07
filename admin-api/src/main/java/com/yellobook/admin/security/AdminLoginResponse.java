package com.yellobook.admin.security;

public record AdminLoginResponse(
        String accessToken
) {
    public static AdminLoginResponse of(String accessToken) {
        return new AdminLoginResponse(accessToken);
    }
}
