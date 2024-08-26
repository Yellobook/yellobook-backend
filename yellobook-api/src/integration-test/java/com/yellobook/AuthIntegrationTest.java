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
}
