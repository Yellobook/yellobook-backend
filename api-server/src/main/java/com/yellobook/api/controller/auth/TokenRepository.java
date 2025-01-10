package com.yellobook.api.controller.auth;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository {
    void setRefreshToken(Long memberId, String refreshToken, long expiresIn);
}
