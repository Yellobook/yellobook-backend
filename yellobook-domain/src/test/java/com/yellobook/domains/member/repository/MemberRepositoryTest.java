package com.yellobook.domains.member.repository;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@EnableJpaAuditing
@DisplayName("Member 도메인 Repository Unit Test")
public class MemberRepositoryTest {
    private final MemberRepository memberRepository;
    private final TestEntityManager entityManager;
    private Member member;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository, TestEntityManager entityManager){
        this.memberRepository = memberRepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void setMember(){
        member = Member.builder()
                .nickname("nickname1")
                .email("email1")
                .profileImage("image1")
                .role(MemberRole.USER)
                .allowance(false)
                .build();
    }

    @Test
    @DisplayName("이메일로 존재하는 사용자 찾기")
    void getExistMember(){
        //given
        String email = member.getEmail();
        entityManager.persist(member);

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        //then
        Assertions.assertThat(optionalMember).isPresent();
        Assertions.assertThat(optionalMember.get().getId()).isEqualTo(member.getId());
        Assertions.assertThat(optionalMember.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("이메일로 존재하는 사용자가 존재 하지 않음을 확인")
    void getNotExistMember(){
        //given
        String email = member.getEmail();

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        //then
        Assertions.assertThat(optionalMember).isEmpty();
    }
}
