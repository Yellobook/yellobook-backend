package com.yellobook.config;

import com.yellobook.domain.auth.jwt.JwtAuthFilter;
import com.yellobook.domain.auth.oauth2.CustomSuccessHandler;
import com.yellobook.domain.auth.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity // 찾아보기
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtAuthFilter jwtAuthFilter;

    @Value("${frontend.base-url}")
    private String frontendBaseURL;

    // 정적 파일은 필터들 타지 않도록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/public/**"),
                new AntPathRequestMatcher("/favicon.ico"),
                new AntPathRequestMatcher("/css/**")
        );
    }

    // cors 구성
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList(frontendBaseURL));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    // oauth 인증용 필터체인
    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**"
                        )
                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // jwt 인가용 필터체인
    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/api/v1/**"
                        )
                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> auth
                        // 관리자 권한이 필요
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        // 이외 요청 모두 jwt 필터를 타도록 설정 -> 로그인하지 않았어도 GUEST 로 Context 저장
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}

