package com.yellobook;

import static fixture.MemberFixture.createMember;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

import com.yellobook.domains.auth.dto.request.AllowanceRequest;
import com.yellobook.domains.auth.service.JwtService;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.support.IntegrationTest;
import com.yellobook.support.annotation.CustomAutowired;
import com.yellobook.support.utils.JwtTestUtil;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("AuthController Integration Test")
public class AuthIntegrationTest extends IntegrationTest {
    @CustomAutowired
    private JwtService jwtService;

    @CustomAutowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("reissueToken 메서드는")
    class Describe_reissueToken {
        final String url = "/api/v1/auth/token/reissue";

        @Nested
        @DisplayName("쿠키로 받은 refresh 토큰이 정상적이라면")
        class Context_refresh_token_is_valid {
            Response response;

            @Value("${cookie.refresh-name}")
            private String refreshTokenName;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var member = createMember();
                memberRepository.save(member);

                Long memberId = member.getId();
                var refreshToken = jwtService.createRefreshToken(memberId);

                response = RestAssured.given()
                        .cookie(refreshTokenName, refreshToken)
                        .log()
                        .all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("200 상태코드로 응답해야 한다.")
            void it_return_200() {
                response.then()
                        .statusCode(200);
            }

            @Test
            @DisplayName("accessToken 을 재발급해야 한다.")
            void it_return_new_access_token() {
                response.then()
                        .body("data", hasKey("accessToken"));
            }
        }

        @Nested
        @DisplayName("쿠키에 refresh 토큰이 없다면")
        class Context_refresh_token_not_exist_in_cookie {
            Response response;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var member = createMember();
                memberRepository.save(member);

                response = RestAssured.given()
                        .log()
                        .all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("401 상태코드로 응답해야 한다.")
            void it_return_200() {
                response.then()
                        .statusCode(401);
            }

            @Test
            @DisplayName("AUTH-010 에러코드를 반환해야 한다.")
            void it_contains_error_code() {
                response.then()
                        .body("code", equalTo("AUTH-010"));
            }

            @Test
            @DisplayName("에러 메시지를 포함해야 한다.")
            void it_contains_message() {
                response.then()
                        .body("message", equalTo("쿠키에 리프레시 토큰이 없습니다."));
            }
        }

        @Nested
        @DisplayName("쿠키에 refresh 토큰이 만료되었다면")
        class Context_refresh_token_is_expired {
            Response response;

            @Value("${cookie.refresh-name}")
            private String refreshTokenName;

            @Value("${jwt.refresh.secret}")
            private String refreshTokenSecret;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var member = createMember();
                memberRepository.save(member);

                Long memberId = member.getId();
                var refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
                var refreshToken = JwtTestUtil.createExpiredToken(memberId, refreshTokenSecretKey);

                response = RestAssured.given()
                        .cookie(refreshTokenName, refreshToken)
                        .log()
                        .all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("401 상태코드로 응답해야 한다.")
            void it_return_200() {
                response.then()
                        .statusCode(401);
            }

            @Test
            @DisplayName("AUTH-005 에러코드를 반환해야 한다.")
            void it_contains_error_code() {
                response.then()
                        .body("code", equalTo("AUTH-005"));
            }

            @Test
            @DisplayName("에러 메시지를 포함해야 한다.")
            void it_contains_message() {
                response.then()
                        .body("message", equalTo("리프레시 토큰의 유효기간이 만료되었습니다."));
            }
        }
    }

    @Nested
    @DisplayName("agreeToTerms 메서드는")
    class Describe_agreeToTerms {
        final String url = "/api/v1/auth/terms";

        @Nested
        @DisplayName("존재하는 회원이라면")
        class Context_exist_member {
            Response response;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var member = createMember(false);
                memberRepository.save(member);

                Long memberId = member.getId();
                var allowanceToken = jwtService.createAllowanceToken(memberId);

                var request = AllowanceRequest.builder()
                        .token(allowanceToken)
                        .build();

                response = RestAssured.given()
                        .log()
                        .all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("200 상태코드로 응답해야 한다.")
            void it_return_200() {
                response.then()
                        .statusCode(200);
            }

            @Test
            @DisplayName("응답 data 에 accessToken 과 refreshToken 포함해야 한다.")
            void it_return_tokens() {
                response.then()
                        .body("data", allOf(hasKey("accessToken"), hasKey("refreshToken")));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회원이라면")
        class Context_not_exist_member {
            Response response;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var allowanceToken = jwtService.createAllowanceToken(9999L);

                var request = AllowanceRequest.builder()
                        .token(allowanceToken)
                        .build();

                response = RestAssured.given()
                        .log()
                        .all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("401 상태코드로 응답해야 한다.")
            void it_return_tokens_with_200() {
                response.then()
                        .statusCode(401);
            }

            @Test
            @DisplayName("AUTH_015 에러코드를 반환해야 한다.")
            void it_contains_error_code() {
                response.then()
                        .body("code", equalTo("AUTH_015"));
            }

            @Test
            @DisplayName("에러 메시지를 포함해야 한다.")
            void it_contains_message() {
                response.then()
                        .body("message", equalTo("가입한 사용자가 존재하지 않습니다."));
            }
        }
    }

    @Nested
    @DisplayName("logout 메서드는")
    class Describe_logout {
        final String url = "/api/v1/auth/logout";

        @Nested
        @DisplayName("로그인한 사용자일 경우")
        class Context_logged_in_member {
            Response response;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var member = createMember();
                memberRepository.save(member);

                Long memberId = member.getId();
                var accessToken = jwtService.createAccessToken(memberId);
                jwtService.createRefreshToken(memberId);

                response = RestAssured.given()
                        .header("Authorization", "Bearer " + accessToken)
                        .log()
                        .all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("204 상태코드로 응답해야 한다.")
            void it_returns_204() {
                response.then()
                        .statusCode(204);
            }
        }

        @Nested
        @DisplayName("accessToken 이 존재하지 않을 경우")
        class Context_access_token_not_provided {
            Response response;

            @Transactional
            @BeforeEach
            void setUpContext() {
                response = RestAssured.given()
                        .log()
                        .all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("401 상태코드로 응답해야 한다.")
            void it_returns_204() {
                response.then()
                        .statusCode(401);
            }
        }

        @Nested
        @DisplayName("로그인이 만료되었을 경우")
        class Context_not_exist_member {
            Response response;

            @Value("${jwt.access.secret}")
            private String accessTokenSecret;

            @Transactional
            @BeforeEach
            void setUpContext() {
                var member = createMember();
                memberRepository.save(member);

                Long memberId = member.getId();
                var accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
                var accessToken = JwtTestUtil.createExpiredToken(memberId, accessTokenSecretKey);

                response = RestAssured.given()
                        .header("Authorization", "Bearer " + accessToken)
                        .log()
                        .all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("401 상태코드로 응답해야 한다.")
            void it_returns_204() {
                response.then()
                        .statusCode(401);
            }
        }
    }
}
