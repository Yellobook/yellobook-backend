package com.yellobook.domains.member.service;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.mapper.MemberMapper;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.yellobook.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberQueryService Unit Test")
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
    @DisplayName("getMemberProfile 메서드는")
    class Describe_GetMemberProfile{
        @Nested
        @DisplayName("사용자가 존재하면")
        class Context_Member_Exist{
            Long memberId;
            ProfileResponse expectResponse;

            @BeforeEach
            void setUp_context(){
                memberId = 1L;
                Member member = createMember();
                List<QueryMemberJoinTeam> queryMemberJoinTeams = createQueryMemberJoinTeam();
                List<ParticipantInfo> participantInfos = queryMemberJoinTeams.stream().map((QueryMemberJoinTeam teamName) -> ParticipantInfo.builder()
                        .teamName(teamName.teamName()).role(teamName.role().getDescription()).build()).toList();
                expectResponse = createProfileResponse(participantInfos);

                when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
                when(participantRepository.getMemberJoinTeam(memberId)).thenReturn(queryMemberJoinTeams);
                when(memberMapper.toProfileResponseDTO(eq(member), anyList())).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("프로필을 반환한다.")
            void it_returns_profile(){
                ProfileResponse response = memberQueryService.getMemberProfile(memberId);

                assertThat(response).isSameAs(expectResponse);
                assertThat(response.memberId()).isEqualTo(expectResponse.memberId());
                verify(memberRepository).findById(memberId);
                verify(participantRepository).getMemberJoinTeam(memberId);
            }
        }

        @Nested
        @DisplayName("사용자가 존재하지 않으면")
        class Context_Member_Not_Exist{
            Long memberId;
            @BeforeEach
            void setUp_context(){
                memberId = 1L;
                when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
            }
            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception(){
                Assertions.assertThrows(CustomException.class, () -> memberQueryService.getMemberProfile(memberId));
                verify(memberRepository).findById(memberId);
            }
        }
    }

    @Nested
    @DisplayName("getAllowanceById 메소드는")
    class Describe_GetAllowanceById{
        @Nested
        @DisplayName("사용자가 존재하면")
        class Context_Member_Exist{
            Long memberId;
            Member member;
            TermAllowanceResponse expectResponse;

            @BeforeEach
            void setUp_context(){
                memberId = 1L;
                member = createMember();
                expectResponse = createAllowanceResponse(member.getAllowance());
                when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
                when(memberMapper.toAllowanceResponseDTO(member.getAllowance())).thenReturn(expectResponse);
            }
            @Test
            @DisplayName("약관 동의 여부를 반환한다")
            void it_returns_allowance(){
                TermAllowanceResponse response = memberQueryService.getAllowanceById(memberId);

                assertThat(response.allowance()).isEqualTo(expectResponse.allowance());
                verify(memberRepository).findById(memberId);
                verify(memberMapper).toAllowanceResponseDTO(member.getAllowance());
            }
        }

        @Nested
        @DisplayName("사용자가 존재하지 않으면")
        class Context_Member_Not_Exist{
            Long memberId;
            @BeforeEach
            void setUp_context(){
                memberId = 1L;
                when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception(){
                Assertions.assertThrows(CustomException.class, () -> memberQueryService.getAllowanceById(memberId));
                verify(memberRepository).findById(memberId);
            }
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


    private List<QueryMemberJoinTeam> createQueryMemberJoinTeam(){
        QueryMemberJoinTeam dto1 = QueryMemberJoinTeam.builder().role(MemberTeamRole.ADMIN).teamName("AAA").build();
        QueryMemberJoinTeam dto2 = QueryMemberJoinTeam.builder().role(MemberTeamRole.ADMIN).teamName("BBB").build();
        return List.of(dto1, dto2);
    }

    private ProfileResponse createProfileResponse(List<ParticipantInfo> participantInfos){
        return ProfileResponse.builder()
                .memberId(1L)
                .nickname("nickname")
                .profileImage("image")
                .email("email")
                .teams(participantInfos)
                .build();
    }

    private TermAllowanceResponse createAllowanceResponse(boolean allowance){
        return TermAllowanceResponse.builder()
                .allowance(allowance)
                .build();
    }


}
