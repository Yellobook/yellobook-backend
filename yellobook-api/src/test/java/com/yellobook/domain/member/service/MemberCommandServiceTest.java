package com.yellobook.domain.member.service;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.error.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberCommandServiceTest {

    @InjectMocks
    private MemberCommandService memberCommandService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("사용자 약관 동의하기 Test")
    class agreeTermTests{
        @Test
        @DisplayName("사용자가 존재하면 약관 비동의 -> 동의로 수정")
        void agreeTerm(){
            //given
            Long memberId = 1L;
            Member member = Member.builder()
                    .nickname("nickname")
                    .email("email")
                    .profileImage("image")
                    .role(MemberRole.USER)
                    .allowance(false)
                    .build();
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

            //when
            memberCommandService.agreeTerm(memberId);

            //then
            assertThat(member.getAllowance()).isTrue();
        }

        @Test
        @DisplayName("사용자가 존재하지 않으면 예외 발생")
        void memberNotExist(){
            //given
            Long memberId = 1L;
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            //when & then
            assertThrows(CustomException.class, () -> memberCommandService.agreeTerm(memberId));
        }
    }




}