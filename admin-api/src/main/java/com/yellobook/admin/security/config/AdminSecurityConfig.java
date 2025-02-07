package com.yellobook.admin.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.admin.security.AdminAccessDeniedHandler;
import com.yellobook.admin.security.AdminAuthenticationEntryPoint;
import com.yellobook.admin.security.AdminJwtAuthenticationFilter;
import com.yellobook.admin.security.AdminJwtAuthorizationFilter;
import com.yellobook.admin.security.AdminJwtProvider;
import com.yellobook.admin.security.AdminUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class AdminSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AdminUserDetailsService adminUserDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(adminUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(
            HttpSecurity http,
            AdminAuthenticationEntryPoint customAuthenticationEntryPoint,
            AdminAccessDeniedHandler adminAccessDeniedHandler,
            AuthenticationManager authenticationManager,
            ObjectMapper objectMapper,
            AdminJwtProvider adminJwtProvider) throws Exception {
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/admin/**"
                        )
                )
                .addFilterAfter(new AdminJwtAuthorizationFilter(adminJwtProvider),
                        ExceptionTranslationFilter.class)
                .addFilter(new AdminJwtAuthenticationFilter(authenticationManager, adminJwtProvider, objectMapper))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/admin/**/auth/login")
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
                        .accessDeniedHandler(adminAccessDeniedHandler)
                );
        return http.build();
    }
}