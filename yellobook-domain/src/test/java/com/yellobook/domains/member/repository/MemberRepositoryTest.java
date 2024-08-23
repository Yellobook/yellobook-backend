package com.yellobook.domains.member.repository;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.support.annotation.RepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@RepositoryTest
@DisplayName("MemberRepository Unit Test")
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

    @Nested
    @DisplayName("findByEmail 메소드는")
    class Describe_findByEmail{
        @Nested
        @DisplayName("해당 이메일이 존재하면")
        class Context_Email_Exist{
            @BeforeEach
            void setUp_context(){
                entityManager.persist(member);
            }

            @Test
            @DisplayName("이메일에 해당하는 사용자를 반환한다.")
            void it_returns_member(){
                Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
                Assertions.assertThat(optionalMember).isPresent();
                Assertions.assertThat(optionalMember.get().getId()).isEqualTo(member.getId());
                Assertions.assertThat(optionalMember.get().getEmail()).isEqualTo(member.getEmail());
            }

        }


        @Nested
        @DisplayName("해당 이메일이 존재하지 않으면")
        class Context_Email_Not_Exist{
            @Test
            @DisplayName("어떤 사용자도 반환하면 안된다.")
            void it_returns_empty(){
                Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());

                Assertions.assertThat(optionalMember).isEmpty();
            }

        }

    }

}
