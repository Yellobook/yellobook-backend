package com.yellobook.domains.auth.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("JwtUtil 테스트")
public class JwtUtilTest {

    @Nested
    @DisplayName("resolveAccessToken 메서드는")
    class Describe_resolveAccessToken {

        @Nested
        @DisplayName("유효한 Authorization 헤더가 제공되었을 때")
        class Context_with_valid_authorization_header {
            MockHttpServletRequest request;

            @BeforeEach
            void setUpContext() {
                request = new MockHttpServletRequest();
                request.addHeader("Authorization", "Bearer validToken");
            }

            @Test
            @DisplayName("accessToken 을 반환해야 한다")
            void it_returns_token() {
                String token = JwtUtil.resolveAccessToken(request);
                assertEquals("validToken", token);
            }
        }

        @Nested
        @DisplayName("Authorization 헤더가 제공되지 않았을 때")
        class Context_no_authorization_header {
            MockHttpServletRequest request;

            @BeforeEach
            void setUpContext() {
                request = new MockHttpServletRequest();
            }

            @Test
            @DisplayName("ACCESS_TOKEN_NOT_FOUND 에러코드가 담긴 CustomException을 던져야 한다")
            void it_should_throw_exception_with_error_code() {
                CustomException exception = assertThrows(CustomException.class, () -> {
                    JwtUtil.resolveAccessToken(request);
                });
                assertEquals(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 Bearer 접두어가 제공되었을 때")
        class Context_invalid_bearer_prefix {
            MockHttpServletRequest request;

            @BeforeEach
            void setUpContext() {
                request = new MockHttpServletRequest();
                request.addHeader("Authorization", "InvalidPrefix validToken");
            }

            @Test
            @DisplayName("ACCESS_TOKEN_NOT_FOUND 에러코드가 담긴 CustomException을 던져야 한다")
            void it_should_throw_Exception() {
                CustomException exception = assertThrows(CustomException.class, () -> {
                    JwtUtil.resolveAccessToken(request);
                });
                assertEquals(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND, exception.getErrorCode());
            }
        }
    }
}
