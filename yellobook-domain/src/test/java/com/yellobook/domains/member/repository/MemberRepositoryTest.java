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
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@EnableJpaAuditing
@DisplayName("Member 도메인 Repository Unit Test")
public class MemberRepositoryTest {
    private final MemberRepository memberRepository;
    private Member member;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
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
        memberRepository.save(member);

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
