package com.yellobook.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.admin.support.response.AdminResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AdminJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AuthenticationManager authenticationManager;
    private final AdminJwtProvider adminJwtProvider;
    private final ObjectMapper objectMapper;

    public AdminJwtAuthenticationFilter(AuthenticationManager authenticationManager, AdminJwtProvider adminJwtProvider,
                                        ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.adminJwtProvider = adminJwtProvider;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/admin/v1/auth/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            AdminLoginRequest adminLoginRequest = new ObjectMapper().readValue(request.getInputStream(),
                    AdminLoginRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(adminLoginRequest.username(), adminLoginRequest.password());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        AdminUserDetails adminUserDetails = (AdminUserDetails) authResult.getPrincipal();
        String accessToken = adminJwtProvider.createAccessToken(adminUserDetails.sub(), adminUserDetails.role());
        response.getWriter()
                .write(objectMapper.writeValueAsString(AdminResponse.success(AdminLoginResponse.of(accessToken))));
    }
}