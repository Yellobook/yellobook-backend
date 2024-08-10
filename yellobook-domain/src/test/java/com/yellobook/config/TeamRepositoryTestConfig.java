package com.yellobook.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
public class TeamRepositoryTestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public EntityManager entityManager() {
        return entityManager;
    }
}
