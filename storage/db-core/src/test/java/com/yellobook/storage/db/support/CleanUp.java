package com.yellobook.storage.db.support;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CleanUp {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public CleanUp(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public void all() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        entityManager.getMetamodel()
                .getEntities()
                .stream()
                .map(entityType -> entityType.getJavaType()
                        .getAnnotation(Table.class)
                        .name())
                .forEach(tableName -> jdbcTemplate.execute("TRUNCATE TABLE " + tableName));
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }
}
