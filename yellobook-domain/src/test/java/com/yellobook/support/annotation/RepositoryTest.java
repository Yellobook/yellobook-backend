package com.yellobook.support.annotation;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.yellobook.support.config.TestJpaConfig;
import com.yellobook.support.config.TestQuerydslConfig;
import com.yellobook.support.config.TestRepositoryConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@Import({
        TestQuerydslConfig.class,
        TestRepositoryConfig.class,
        TestJpaConfig.class
})
@ActiveProfiles("test")
@Sql(scripts = "classpath:cleanup.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public abstract class RepositoryTest {
    private static final String MYSQL_CONTAINER_IMAGE_TAG = "mysql:8.0.32"; // MySQL 8.0 이미지

    static final MySQLContainer mysql;

    static {
        mysql = new MySQLContainer<>(MYSQL_CONTAINER_IMAGE_TAG)
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);

        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Mysql
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}
