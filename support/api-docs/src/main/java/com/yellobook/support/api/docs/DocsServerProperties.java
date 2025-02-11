package com.yellobook.support.api.docs;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("docs.server")
public record DocsServerProperties(
        String apiUrl,
        String adminUrl
) {
}
