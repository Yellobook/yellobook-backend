package com.yellobook.domains.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.ErrorCode;
import com.yellobook.response.ResponseFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Object errorObj = request.getAttribute("error");
        ErrorCode errorCode =
                errorObj instanceof ErrorCode ? (ErrorCode) errorObj : AuthErrorCode.AUTHENTICATION_FAILED;
        ResponseEntity<Object> errorResponse = ResponseFactory.failure(errorCode);
        response.setContentType("application/json");
        // charset=UTF-8 을 붙이지 않으면 message 가 ??? 로 깨져서 표시된다.
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorResponse.getStatusCode()
                .value());
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(errorResponse.getBody()));
        response.getWriter()
                .flush();
    }
}
