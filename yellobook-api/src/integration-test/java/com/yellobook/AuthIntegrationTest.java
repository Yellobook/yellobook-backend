package com.yellobook;

import com.yellobook.domains.auth.dto.request.AllowanceRequest;
import com.yellobook.domains.auth.service.JwtService;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.support.annotation.CustomAutowired;
import com.yellobook.support.annotation.IntegrationTest;
import com.yellobook.support.utils.JwtTestUtil;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import io.restassured.RestAssured;

import static fixture.MemberFixture.*;
import static org.hamcrest.Matchers.*;

@IntegrationTest
@DisplayName("AuthController Integration Test")
public class AuthIntegrationTest {
    private final static String TEST_CONTAINER_IMAGE_TAG = "postgres:16";
    private static final String REDIS_DOCKER_IMAGE_TAG = "redis:7-alpine";
    private static final int REDIS_PORT = 6379;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(TEST_CONTAINER_IMAGE_TAG)
            .withInitScript("schema.sql");

    static GenericContainer<?> redis = new GenericContainer<>(REDIS_DOCKER_IMAGE_TAG)
            .withExposedPorts(REDIS_PORT);

    @LocalServerPort
    private int port;

    @CustomAutowired
    private JwtService jwtService;

    @CustomAutowired
    private MemberRepository memberRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        redis.start();
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @AfterAll
    static void afterAll() {
        redis.stop();
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(REDIS_PORT));
    }


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
            void prepare() {
                var member = createMember(false);
                memberRepository.save(member);

                Long memberId = member.getId();
                var accessToken = jwtService.createAccessToken(memberId);
                var refreshToken = jwtService.createRefreshToken(memberId);

                response = RestAssured.given()
                        .cookie(refreshTokenName, refreshToken)
                        .header("Authorization", "Bearer " + accessToken)
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("200 상태코드로 응답해야 한다.")
            void it_return_200() {
                response.then().statusCode(200);
            }

            @Test
            @DisplayName("accessToken 을 재발급해야 한다.")
            void it_return_new_access_token() {
                response.then().body("data", hasKey("accessToken"));
            }
        }

        @Nested
        @DisplayName("쿠키에 refresh 토큰이 없다면")
        class Context_refresh_token_not_exist_in_cookie {
            Response response;

            @Transactional
            @Commit
            @BeforeEach
            void prepare() {
                var member = createMember(false);
                memberRepository.save(member);

                Long memberId = member.getId();
                var accessToken = jwtService.createAccessToken(memberId);

                response = RestAssured.given()
                        .header("Authorization", "Bearer " + accessToken)
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(url);
            }

            @Test
            @DisplayName("401 상태코드로 응답해야 한다.")
            void it_return_200() {
                response.then().statusCode(401);
            }

            @Test
            @DisplayName("AUTH-010 에러코드를 반환해야 한다.")
            void it_contains_error_code() {
                response.then().body("code", equalTo("AUTH-010"));
            }

            @Test
            @DisplayName("에러 메시지를 포함해야 한다.")
            void it_contains_message() {
                response.then().body("message", equalTo("쿠키에 refreshToken 이 없습니다."));
            }
        }
    }


}
