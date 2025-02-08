package com.yellobook.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.response.AdminResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;

    public AdminAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("AuthException : {}", authException.getMessage(), authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        AdminResponse<?> errorResponse = AdminResponse.error(AdminErrorType.AUTH_FAILED);
        response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
    }
}
