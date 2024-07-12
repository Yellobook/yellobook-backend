package com.yellobook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .exposedHeaders("Set-Cookie", "Authorization")
                .allowedOrigins(frontendBaseUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS 추가
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
