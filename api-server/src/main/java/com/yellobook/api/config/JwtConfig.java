package com.yellobook.api.config;

import com.yellobook.api.support.auth.JwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    private final JwtProperties properties;

    public JwtConfig(JwtProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "accessTokenSecretKey")
    public SecretKey accessTokenSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(properties.accessToken()
                .secret()));
    }

    @Bean(name = "refreshTokenSecretKey")
    public SecretKey refreshTokenSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(properties.refreshToken()
                .secret()));
    }
}
