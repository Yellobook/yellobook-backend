package com.yellobook.api.config;

import com.yellobook.api.support.ApiMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ApiMemberArgumentResolver teamMemberArgumentResolver;

    public WebConfig(ApiMemberArgumentResolver teamMemberArgumentResolver) {
        this.teamMemberArgumentResolver = teamMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(teamMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "https://yellobook.site",
                        "https://api.yellobook.site"
                );
    }
}
