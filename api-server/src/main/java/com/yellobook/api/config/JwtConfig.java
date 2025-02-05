package com.yellobook.api.config;

import com.yellobook.api.support.auth.JwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

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

    @PostConstruct
    public void logJwtProperties() {
        logger.info("✅ JwtProperties Loaded: {}", properties);
        if (properties.accessToken() == null) {
            logger.error("❌ accessToken is NULL!");
        } else {
            logger.info("🔑 accessToken.secret: {}", properties.accessToken()
                    .secret());
            logger.info("⌛ accessToken.expiresIn: {}", properties.accessToken()
                    .expiresIn());
        }

        if (properties.refreshToken() == null) {
            logger.error("❌ refreshToken is NULL!");
        } else {
            logger.info("🔑 refreshToken.secret: {}", properties.refreshToken()
                    .secret());
            logger.info("⌛ refreshToken.expiresIn: {}", properties.refreshToken()
                    .expiresIn());
        }
    }
}
