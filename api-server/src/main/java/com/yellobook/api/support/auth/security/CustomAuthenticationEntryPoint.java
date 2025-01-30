package com.yellobook.api.support.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import com.yellobook.api.support.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof AuthException e) {
            switch (e.getErrorType()
                    .getLogLevel()) {
                case ERROR -> log.error("AuthException : {}", e.getMessage(), e);
                case WARN -> log.warn("AuthException : {}", e.getMessage(), e);
                case INFO -> log.info("AuthException : {}", e.getMessage(), e);
            }
        } else {
            log.error("AuthException : {}", authException.getMessage(), authException);
        }
        ApiResponse<?> errorResponse = ApiResponse.error(AuthErrorType.AUTHORIZE_FAILED);
        response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
    }
}
