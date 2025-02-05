package com.yellobook.api.support.auth;

import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtStorageHandler {

    private final CachedJwtRepository cachedJwtRepository;

    public JwtStorageHandler(CachedJwtRepository cachedJwtRepository) {
        this.cachedJwtRepository = cachedJwtRepository;
    }

    public void addAccessTokenToBlacklist(String accessToken, Date expiration) {
        cachedJwtRepository.addAccessTokenToBlacklist(accessToken, expiration);
    }

    public boolean isAccessTokenInBlacklist(String accessToken) {
        return cachedJwtRepository.isAccessTokenInBlacklist(accessToken);
    }

    public void deleteRefreshToken(Long memberId) {
        cachedJwtRepository.removeRefreshToken(memberId);
    }

    public String getRefreshToken(Long memberId) {
        return cachedJwtRepository.findRefreshToken(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorType.REFRESH_TOKEN_NOT_FOUND));
    }
}
