package com.yellobook.admin.security;

public record AdminLoginRequest(
        String username,
        String password
) {
}
