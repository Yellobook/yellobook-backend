package com.yellobook.support;

import com.yellobook.domains.auth.service.JwtService;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.support.annotation.CustomAutowired;
import com.yellobook.support.annotation.IntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@IntegrationTest
public abstract class IntegrationTestSupport {

    private final static String TEST_CONTAINER_IMAGE_TAG = "postgres:16";
    private static final String REDIS_DOCKER_IMAGE_TAG = "redis:7-alpine";
    private static final int REDIS_PORT = 6379;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(TEST_CONTAINER_IMAGE_TAG);

    static GenericContainer<?> redis = new GenericContainer<>(REDIS_DOCKER_IMAGE_TAG)
            .withExposedPorts(REDIS_PORT);

    @LocalServerPort
    protected int port;

    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    OAuth2AuthorizedClientRepository authorizedClientRepository;

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
