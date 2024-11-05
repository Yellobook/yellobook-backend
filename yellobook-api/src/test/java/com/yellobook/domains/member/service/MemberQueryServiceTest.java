package com.yellobook.domains.member.service;

import static com.yellobook.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.member.dto.response.CurrentTeamResponse;
import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.mapper.MemberMapper;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.MemberErrorCode;
import com.yellobook.error.exception.CustomException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberQueryService Unit Test")
class MemberQueryServiceTest {
    @InjectMocks
    private MemberQueryService memberQueryService;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private MemberMapper memberMapper;

    private List<QueryMemberJoinTeam> createQueryMemberJoinTeam() {
        QueryMemberJoinTeam dto1 = QueryMemberJoinTeam.builder()
                .role(TeamMemberRole.ADMIN)
                .teamName("AAA")
                .build();
        QueryMemberJoinTeam dto2 = QueryMemberJoinTeam.builder()
                .role(TeamMemberRole.ADMIN)
                .teamName("BBB")
                .build();
        return List.of(dto1, dto2);
    }

    private ProfileResponse createProfileResponse(List<ParticipantInfo> participantInfos) {
        return ProfileResponse.builder()
                .memberId(1L)
                .nickname("nickname")
                .profileImage("image")
                .email("email")
                .teams(participantInfos)
                .build();
    }

    private TermAllowanceResponse createAllowanceResponse(boolean allowance) {
        return TermAllowanceResponse.builder()
                .allowance(allowance)
                .build();
    }

    @Nested
    @DisplayName("getMemberProfile 메소드는")
    class Describe_GetMemberProfile {
        @Nested
        @DisplayName("사용자가 존재하면")
        class Context_member_exist {
            Long memberId;
            ProfileResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                memberId = 1L;
                Member member = createMember();
                List<QueryMemberJoinTeam> queryMemberJoinTeams = createQueryMemberJoinTeam();
                List<ParticipantInfo> participantInfos = queryMemberJoinTeams.stream()
                        .map((QueryMemberJoinTeam teamName) -> ParticipantInfo.builder()
                                .teamName(teamName.teamName())
                                .role(teamName.role()
                                        .getDescription())
                                .build())
                        .toList();
                expectResponse = createProfileResponse(participantInfos);

                when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
                when(participantRepository.getMemberJoinTeam(memberId)).thenReturn(queryMemberJoinTeams);
                when(memberMapper.toProfileResponseDTO(eq(member), anyList())).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("프로필을 반환한다.")
            void it_returns_profile() {
                ProfileResponse response = memberQueryService.getMemberProfile(memberId);

                assertThat(response).isSameAs(expectResponse);
                assertThat(response.memberId()).isEqualTo(expectResponse.memberId());
                verify(memberRepository).findById(memberId);
                verify(participantRepository).getMemberJoinTeam(memberId);
            }
        }

        @Nested
        @DisplayName("사용자가 존재하지 않으면")
        class Context_member_not_exist {
            Long memberId;

            @BeforeEach
            void setUpContext() {
                memberId = 1L;
                when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                assertThrows(CustomException.class, () -> memberQueryService.getMemberProfile(memberId));
                verify(memberRepository).findById(memberId);
            }
        }
    }

    @Nested
    @DisplayName("getAllowanceById 메소드는")
    class Describe_GetAllowanceById {
        @Nested
        @DisplayName("사용자가 존재하면")
        class Context_member_exist {
            Long memberId;
            Member member;
            TermAllowanceResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                memberId = 1L;
                member = createMember();
                expectResponse = createAllowanceResponse(member.getAllowance());
                when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
                when(memberMapper.toAllowanceResponseDTO(member.getAllowance())).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("약관 동의 여부를 반환한다")
            void it_returns_allowance() {
                TermAllowanceResponse response = memberQueryService.getAllowanceById(memberId);

                assertThat(response.allowance()).isEqualTo(expectResponse.allowance());
                verify(memberRepository).findById(memberId);
                verify(memberMapper).toAllowanceResponseDTO(member.getAllowance());
            }
        }

        @Nested
        @DisplayName("사용자가 존재하지 않으면")
        class Context_member_not_exist {
            Long memberId;

            @BeforeEach
            void setUpContext() {
                memberId = 1L;
                when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("CustomException 이 발생해야 한다.")
            void it_throws_exception() {
                assertThrows(CustomException.class, () -> memberQueryService.getAllowanceById(memberId));
                verify(memberRepository).findById(memberId);
            }
        }
    }

    @Nested
    @DisplayName("getMemberCurrentTeam 메소드는")
    class Describe_GetMemberCurrentTeam {
        @Nested
        @DisplayName("존재하는 팀이라면")
        class Context_with_team {
            final Long teamId = 99L;
            final String teamName = "옐로북팀";

            @BeforeEach
            void setUpContext() throws NoSuchFieldException, IllegalAccessException {
                CurrentTeamResponse response = CurrentTeamResponse.builder()
                        .teamId(teamId)
                        .teamName(teamName)
                        .build();
                Team team = createTeam(teamName);
                Field teamIdField = Team.class.getDeclaredField("id");
                teamIdField.setAccessible(true);
                teamIdField.set(team, teamId);

                when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
                when(memberMapper.toCurrentTeamResponse(team)).thenReturn(response);
            }

            @Test
            @DisplayName("사용자가 현재 위치한 팀Id 와 팀 이름을 반환한다.")
            void it_returns_current_teamId_and_teamName() {
                var response = memberQueryService.getMemberCurrentTeam(teamId);
                assertThat(response.teamId()).isEqualTo(teamId);
                assertThat(response.teamName()).isEqualTo(teamName);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 팀이라면")
        class Context_with_non_exist_team {
            @BeforeEach
            void setUpContext() {
                when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("CustomException이 발생해야 한다.")
            void it_throws_exception() {
                CustomException exception = assertThrows(
                        CustomException.class,
                        () -> memberQueryService.getMemberCurrentTeam(1L)
                );

                assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_TEAM_NOT_FOUND);
            }

        }
    }


}
