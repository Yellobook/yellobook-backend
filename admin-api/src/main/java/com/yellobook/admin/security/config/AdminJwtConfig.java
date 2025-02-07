package com.yellobook.admin.security.config;

import com.yellobook.admin.security.AdminJwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminJwtConfig {
    private final AdminJwtProperties properties;

    public AdminJwtConfig(AdminJwtProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "adminAccessTokenSecretKey")
    public SecretKey accessTokenSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(properties.accessToken()
                .secret()));
    }
}
