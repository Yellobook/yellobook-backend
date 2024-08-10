package com.yellobook.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ScheduleRepositoryTestConfig {
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public ScheduleRepository scheduleRepository(JPAQueryFactory jpaQueryFactory) {
        return new ScheduleRepository(jpaQueryFactory);
    }
}
