package com.yellobook.api.support.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenManagerTest {
    private JwtManager tokenManager;
    private CachedJwtRepository cachedTokenRepository;
    private JwtProperties tokenProperties;

    @BeforeEach
    void setup() {
        tokenProperties = mock(JwtProperties.class);
        cachedTokenRepository = mock(CachedJwtRepository.class);

        JwtProperties.AccessToken accessTokenProperties = new JwtProperties.AccessToken(
                "accessSecretKeyInBase64", 7200
        );
        JwtProperties.RefreshToken refreshTokenProperties = new JwtProperties.RefreshToken(
                "refreshSecretKeyInBase64", 604800
        );

        when(tokenProperties.accessToken()).thenReturn(accessTokenProperties);
        when(tokenProperties.refreshToken()).thenReturn(refreshTokenProperties);

        tokenManager = new JwtManager(tokenProperties, cachedTokenRepository);
        tokenManager.initialize();
    }

    @Nested
    class AccessToken_생성_테스트 {

        @Test
        void 정상적으로_액세스_토큰을_생성한다() {
            Long memberId = 123L;

            String accessToken = tokenManager.createAccessToken(memberId);

            assertThat(accessToken).isNotNull();
        }
    }

    @Nested
    class RefreshToken_생성_테스트 {

        @Test
        void 정상적으로_리프레시_토큰을_생성하고_캐시에_저장한다() {
            Long memberId = 123L;

            String refreshToken = tokenManager.createRefreshToken(memberId);

            assertThat(refreshToken).isNotNull();
            verify(cachedTokenRepository, times(1))
                    .saveRefreshToken(eq(memberId), eq(refreshToken), eq(Duration.ofSeconds(604800)));
        }
    }

    @Nested
    class AccessToken_만료_검사 {

        @Test
        void 만료된_액세스_토큰은_만료로_판단한다() {
            SecretKey expiredAccessKey = Keys.hmacShaKeyFor("expiredAccessSecret".getBytes());
            String expiredAccessToken = Jwts.builder()
                    .issuedAt(new Date(System.currentTimeMillis() - 7200000)) // 2시간 전
                    .expiration(new Date(System.currentTimeMillis() - 3600000)) // 1시간 전
                    .signWith(expiredAccessKey)
                    .compact();

            boolean isExpired = tokenManager.isAccessTokenExpired(expiredAccessToken);

            assertThat(isExpired).isTrue();
        }

        @Test
        void 유효한_액세스_토큰은_만료되지_않는다() {
            Long memberId = 123L;

            String accessToken = tokenManager.createAccessToken(memberId);

            boolean isExpired = tokenManager.isAccessTokenExpired(accessToken);

            assertThat(isExpired).isFalse();
        }
    }

    @Nested
    class RefreshToken_만료_검사 {

        @Test
        void 만료된_리프레시_토큰은_만료로_판단한다() {
            SecretKey expiredRefreshKey = Keys.hmacShaKeyFor("expiredRefreshSecret".getBytes());
            String expiredRefreshToken = Jwts.builder()
                    .issuedAt(new Date(System.currentTimeMillis() - 604800000)) // 7일 전
                    .expiration(new Date(System.currentTimeMillis() - 3600000)) // 1시간 전
                    .signWith(expiredRefreshKey)
                    .compact();

            boolean isExpired = tokenManager.isRefreshTokenExpired(expiredRefreshToken);

            assertThat(isExpired).isTrue();
        }

        @Test
        void 유효한_리프레시_토큰은_만료되지_않는다() {
            Long memberId = 123L;

            String refreshToken = tokenManager.createRefreshToken(memberId);

            boolean isExpired = tokenManager.isRefreshTokenExpired(refreshToken);

            assertThat(isExpired).isFalse();
        }
    }

    @Nested
    class JWT_토큰_파싱_테스트 {

        @Test
        void 유효하지_않은_토큰_파싱_시도시_예외를_발생시킨다() {
            String invalidToken = "invalid.token.value";

            assertThatThrownBy(() -> tokenManager.isAccessTokenExpired(invalidToken))
                    .isInstanceOf(Exception.class);
        }

        @Test
        void 토큰에_잘못된_키를_사용할_경우_예외를_발생시킨다() {
            SecretKey wrongKey = Keys.hmacShaKeyFor("wrongSecretKey".getBytes());
            Long memberId = 123L;
            String accessToken = tokenManager.createAccessToken(memberId);

            assertThatThrownBy(() -> Jwts.parserBuilder()
                    .setSigningKey(wrongKey)
                    .build()
                    .parseClaimsJws(accessToken))
                    .isInstanceOf(Exception.class);
        }
    }
}
