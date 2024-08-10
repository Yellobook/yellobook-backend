package com.yellobook.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.team.repository.ParticipantCustomRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ParticipantRepositoryTestConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public ParticipantCustomRepositoryImpl participantCustomRepositoryImpl(EntityManager em) {
        return new ParticipantCustomRepositoryImpl(em);
    }
}
