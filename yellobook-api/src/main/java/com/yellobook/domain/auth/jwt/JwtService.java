package com.yellobook.domain.auth.jwt;

import com.yellobook.code.AuthErrorCode;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.enums.MemberRole;
import com.yellobook.exception.CustomException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    MemberRepository memberRepository;

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.expires-in}")
    private int accessTokenExpiresIn;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.expires-in}")
    private int refreshExpiresIn;

    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void initialize() {
        System.out.println("accessTokenSecret = " + accessTokenSecret);
        System.out.println("refreshTokenSecret = " + refreshTokenSecret);

        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String createAccessToken(String email, MemberRole role) {
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiresIn))
                .signWith(accessTokenSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        return null;
    }


    public boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(accessTokenSecretKey).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    public Member getMemberFromToken(String token) {
        String email = getEmailFromToken(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));
    }

    private String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(accessTokenSecretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role",String.class);
    }
}
