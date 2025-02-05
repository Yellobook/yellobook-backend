package com.yellobook.storage.db.support;


import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(scripts = "/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class RepositoryTest {

    static final String MYSQL_CONTAINER_IMAGE_TAG = "mysql:8.0.32";

    static final MySQLContainer mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>(MYSQL_CONTAINER_IMAGE_TAG)
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        mysqlContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String p6spyJdbcUrl = mysqlContainer.getJdbcUrl()
                .replace("jdbc:", "jdbc:p6spy:");
        registry.add("spring.datasource.url", () -> p6spyJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.p6spy.engine.spy.P6SpyDriver");
    }
}
