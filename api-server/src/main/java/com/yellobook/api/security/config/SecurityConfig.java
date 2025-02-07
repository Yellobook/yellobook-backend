package com.yellobook.api.security.config;

import com.yellobook.api.security.CustomAccessDeniedHandler;
import com.yellobook.api.security.CustomAuthenticationEntryPoint;
import com.yellobook.api.security.JwtAuthorizationFilter;
import com.yellobook.api.security.JwtProvider;
import com.yellobook.api.security.JwtService;
import com.yellobook.api.security.oauth2.CustomOAuth2AttributeConverter;
import com.yellobook.api.security.oauth2.CustomOAuth2SuccessHandler;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        "/public/**",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(
                List.of(
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "https://yellobook.site",
                        "https://api.yellobook.site"
                )
        );
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.addAllowedHeader("*");
        config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public DefaultOAuth2UserService oAuth2UserService(
            CustomOAuth2AttributeConverter attributeConverter) {
        DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
        userService.setAttributesConverter(attributeConverter);
        return userService;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(
            HttpSecurity http,
            CustomOAuth2SuccessHandler customSuccessHandler,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            JwtProvider adminJwtProvider,
            JwtService jwtService
    ) throws Exception {
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**",
                                "/api/**"
                        )
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customSuccessHandler))
                .addFilterAfter(new JwtAuthorizationFilter(jwtService, adminJwtProvider),
                        ExceptionTranslationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(
                                "/api/v1",
                                "/api/v1/health",
                                "/api/v1/auth/refresh",
                                "/login/oauth2/code/**",
                                "/api/v1/dev/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );
        return http.build();
    }

}