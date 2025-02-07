package com.yellobook.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.admin.account")
public record SuperAdminProperties(
        String username,
        String password
) {
}
