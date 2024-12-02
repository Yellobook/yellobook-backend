package com.yellobook.core.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("integration-test")
public abstract class IntegrationTest {

    private static final String MYSQL_CONTAINER_IMAGE_TAG = "mysql:8.0.32"; // MySQL 8.0 이미지
    private static final String REDIS_DOCKER_IMAGE_TAG = "redis:7.0.12";
    private static final int REDIS_PORT = 6379;

    static final MySQLContainer mysql;
    static final GenericContainer redis;

    static {
        mysql = new MySQLContainer<>(MYSQL_CONTAINER_IMAGE_TAG)
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");

        redis = new GenericContainer<>(REDIS_DOCKER_IMAGE_TAG)
                .withExposedPorts(REDIS_PORT);
        mysql.start();
        redis.start();
    }

    @LocalServerPort
    protected int port;

    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    OAuth2AuthorizedClientRepository authorizedClientRepository;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Mysql
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);

        // Redis
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(REDIS_PORT));
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }
}
