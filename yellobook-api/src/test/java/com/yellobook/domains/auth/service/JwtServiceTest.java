package com.yellobook.domains.auth.service;

import com.yellobook.domains.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RedisAuthService redisAuthService;


    @Nested
    @DisplayName("isAccessTokenExpired 메서드는")
    class Describe_isAccessTokenExpired {

        @Nested
        @DisplayName("accessToken 이 만료되었을 경우")
        class Context_with_expired_accessToken {

            @BeforeEach
            void setUpContext() {

            }

            @Test
            @DisplayName("")
            void it_should_throw_exception_for_expired_token() {

            }
        }
    }
}