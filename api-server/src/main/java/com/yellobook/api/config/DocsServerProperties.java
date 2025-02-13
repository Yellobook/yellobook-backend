package com.yellobook.api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("docs.server")
public record DocsServerProperties(
        String apiUrl,
        String adminUrl
) {
}
