package com.yellobook.domain.member.service;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domain.member.mapper.MemberMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.dto.MemberJoinTeamDTO;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.yellobook.domain.member.dto.MemberResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberQueryServiceTest {
    @InjectMocks
    private MemberQueryService memberQueryService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private MemberMapper memberMapper;

    @Nested
    @DisplayName("사용자 프로필 조회 Test")
    class GetMemberProfileTests{
        @Test
        @DisplayName("사용자가 존재하면 프로필 반환")
        void getMemberProfile(){
            //given
            Long memberId = 1L;
            Member member = createMember();
            List<MemberJoinTeamDTO> memberJoinTeamDTOS = createMemberJoinTeamDTO();
            List<ParticipantInfo> participantInfos = memberJoinTeamDTOS.stream().map(ParticipantInfo::new).toList();
            ProfileResponseDTO expectResponse = createProfileResponseDTO(participantInfos);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(participantRepository.getMemberJoinTeam(memberId)).thenReturn(memberJoinTeamDTOS);
            when(memberMapper.toProfileResponseDTO(eq(member), anyList())).thenReturn(expectResponse);

            //when
            ProfileResponseDTO response = memberQueryService.getMemberProfile(memberId);

            //then
            assertThat(response).isSameAs(expectResponse);
            assertThat(response.getMemberId()).isEqualTo(expectResponse.getMemberId());
            verify(memberRepository).findById(memberId);
            verify(participantRepository).getMemberJoinTeam(memberId);
        }

        @Test
        @DisplayName("사용자가 존재하지 않으면 예외 반환")
        void getNotExistMemberProfile(){
            //given
            Long memberId = 1L;
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            //when & then
            Assertions.assertThrows(CustomException.class, () -> memberQueryService.getMemberProfile(memberId));
            verify(memberRepository).findById(memberId);
        }
    }

    @Nested
    @DisplayName("사용자 약관 동의 여부 확인 Test")
    class GetAllowanceByIdTests{
        @Test
        @DisplayName("사용자가 존재할 때 약관 동의 여부가 null 아니면 약관 동의 여부 반환")
        void getExistMemberAllowance(){
            //given
            Long memberId = 1L;
            Member member = createMember();
            AllowanceResponseDTO expectResponse = createAllowanceResponseDTO(member.getAllowance());
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(memberMapper.toAllowanceResponseDTO(member.getAllowance())).thenReturn(expectResponse);

            //when
            AllowanceResponseDTO response = memberQueryService.getAllowanceById(memberId);

            //then
            assertThat(response.isAllowance()).isEqualTo(expectResponse.isAllowance());
            verify(memberRepository).findById(memberId);
            verify(memberMapper).toAllowanceResponseDTO(member.getAllowance());
        }

        @Test
        @DisplayName("사용자가 존재하지 않으면 예외 반환")
        void getNotExistMemberAllowance(){
            //given
            Long memberId = 1L;
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            //when & then
            Assertions.assertThrows(CustomException.class, () -> memberQueryService.getMemberProfile(memberId));
            verify(memberRepository).findById(memberId);
        }
    }

    private Member createMember(){
        return Member.builder()
                .nickname("nickname")
                .email("email")
                .profileImage("image")
                .role(MemberRole.USER)
                .allowance(false)
                .build();
    }


    private List<MemberJoinTeamDTO> createMemberJoinTeamDTO(){
        MemberJoinTeamDTO dto1 = MemberJoinTeamDTO.builder().role(MemberTeamRole.ADMIN).teamName("AAA").build();
        MemberJoinTeamDTO dto2 = MemberJoinTeamDTO.builder().role(MemberTeamRole.ADMIN).teamName("BBB").build();
        return List.of(dto1, dto2);
    }

    private ProfileResponseDTO createProfileResponseDTO(List<ParticipantInfo> participantInfos){
        return ProfileResponseDTO.builder()
                .memberId(1L)
                .nickname("nickname")
                .profileImage("image")
                .email("email")
                .teams(participantInfos)
                .build();
    }

    private AllowanceResponseDTO createAllowanceResponseDTO(boolean allowance){
        return AllowanceResponseDTO.builder()
                .allowance(allowance)
                .build();
    }


}
