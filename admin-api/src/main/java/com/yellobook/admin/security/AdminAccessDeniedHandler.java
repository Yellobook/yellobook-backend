package com.yellobook.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.response.AdminResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AdminAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;

    public AdminAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("AuthException : {}", accessDeniedException.getMessage(), accessDeniedException);
        AdminResponse<?> errorResponse = AdminResponse.error(AdminErrorType.FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
    }
}
