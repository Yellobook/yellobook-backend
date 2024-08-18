package com.yellobook.support.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.schedule.repository.ScheduleRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestRepositoryConfig {
    @Bean
    public ScheduleRepository scheduleRepository(JPAQueryFactory jpaQueryFactory) {
        return new ScheduleRepository(jpaQueryFactory);
    }
}
