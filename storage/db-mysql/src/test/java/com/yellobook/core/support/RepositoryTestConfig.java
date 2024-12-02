package com.yellobook.core.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domains.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@TestConfiguration
@EnableJpaAuditing
public class RepositoryTestConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    // JpaRepository가 아닌 클래스는 수동 빈 등록
    @Bean
    public ScheduleRepository scheduleRepository(JPAQueryFactory jpaQueryFactory) {
        return new ScheduleRepository(jpaQueryFactory);
    }
}
