package com.yellobook.api.support.auth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.redirect")
public record RedirectProperties(
        String authenticatedRedirectUrl,
        String termRedirectUrl
) {
}
