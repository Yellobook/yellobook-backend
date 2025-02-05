package com.yellobook.api.support.auth;

import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final JwtStorageHandler jwtStorageHandler;

    public JwtService(JwtProvider jwtProvider, JwtStorageHandler jwtStorageHandler) {
        this.jwtProvider = jwtProvider;
        this.jwtStorageHandler = jwtStorageHandler;
    }

    public String reissueAccessToken(String refreshToken, AppMemberRole role) {
        Long memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken);
        String storedRefreshToken = jwtStorageHandler.getRefreshToken(memberId);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new AuthException(AuthErrorType.REFRESH_TOKEN_COOKIE_MISMATCH);
        }
        return jwtProvider.createAccessToken(
                new AccessTokenPayload(
                        memberId,
                        role
                )
        );
    }

    public void invalidateTokens(Long memberId, String accessToken) {
        Date expiration = jwtProvider.getAccessTokenExpiresIn(accessToken);
        jwtStorageHandler.addAccessTokenToBlacklist(accessToken, expiration);
        jwtStorageHandler.deleteRefreshToken(memberId);
    }
}
