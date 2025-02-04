package com.yellobook.domains.member.repository;

import static fixture.MemberFixture.createMember;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.support.RepositoryTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MemberRepository Unit Test")
public class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setMember() {
        resetAutoIncrement();
    }

    @Nested
    @DisplayName("findByEmail 메소드는")
    class Describe_findByEmail {
        @Nested
        @DisplayName("해당 이메일이 존재하면")
        class Context_email_exist {
            Member member;

            @BeforeEach
            void setUpContext() {
                member = em.persist(createMember());
            }

            @Test
            @DisplayName("이메일에 해당하는 사용자를 반환한다.")
            void it_returns_member() {
                Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
                Assertions.assertThat(optionalMember)
                        .isPresent();
                Assertions.assertThat(optionalMember.get()
                                .getId())
                        .isEqualTo(member.getId());
                Assertions.assertThat(optionalMember.get()
                                .getEmail())
                        .isEqualTo(member.getEmail());
            }

        }

        @Nested
        @DisplayName("해당 이메일이 존재하지 않으면")
        class Context_email_not_exist {
            Member member;

            @BeforeEach
            void setUpContext() {
                member = em.persist(createMember());
            }

            @Test
            @DisplayName("어떤 사용자도 반환하지 않는다.")
            void it_returns_empty() {
                String nonExistEmail = "nonExistEmail@gmail.com";
                Optional<Member> optionalMember = memberRepository.findByEmail(nonExistEmail);

                Assertions.assertThat(optionalMember)
                        .isEmpty();
            }
        }

    }

}
