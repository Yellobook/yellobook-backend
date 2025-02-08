package com.yellobook.api.security;

import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.security.error.AuthException;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtProvider adminJwtProvider;
    private final JwtCachedRepository jwtCachedRepository;

    public JwtService(JwtProvider adminJwtProvider, JwtCachedRepository jwtCachedRepository) {
        this.adminJwtProvider = adminJwtProvider;
        this.jwtCachedRepository = jwtCachedRepository;
    }

    public String reissueAccessToken(String refreshToken) {
        long memberId = jwtCachedRepository.findMemberIdFromRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorType.REFRESH_TOKEN_NOT_FOUND));
        return adminJwtProvider.createAccessToken(memberId);
    }

    public boolean isAccessTokenInBlacklist(String accessToken) {
        return jwtCachedRepository.isAccessTokenInBlacklist(accessToken);
    }

    public void invalidateTokens(String accessToken, String refreshToken) {
        Date accessTokenExpiresIn = adminJwtProvider.getAccessTokenExpiresIn(accessToken);
        jwtCachedRepository.addAccessTokenToBlacklist(accessToken, accessTokenExpiresIn);
        jwtCachedRepository.removeRefreshToken(refreshToken);
    }
}
