package com.yellobook.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@Import(RepositoryTestConfig.class)
@ActiveProfiles("test")
@Getter
public abstract class RepositoryTest {
    @Autowired
    protected TestEntityManager em;

    private static final String MYSQL_CONTAINER_IMAGE_TAG = "mysql:8.0.32"; // MySQL 8.0 이미지

    private static final MySQLContainer mysql;

    static {
        mysql = new MySQLContainer<>(MYSQL_CONTAINER_IMAGE_TAG)
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);

        mysql.start();
    }

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        // Mysql
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    protected void resetAutoIncrement() {
        EntityManager em = this.em.getEntityManager();
        em.createNativeQuery("ALTER TABLE inform_mentions AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE inform_comments AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE informs AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE order_comments AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE order_mentions AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE orders AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE products AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE inventories AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE participants AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE teams AUTO_INCREMENT = 1")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE members AUTO_INCREMENT = 1")
                .executeUpdate();
    }
}

