package com.yellobook.team.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yellobook.TeamMemberRole;
import com.yellobook.auth.dto.InvitationResponse;
import com.yellobook.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.auth.service.TeamService;
import com.yellobook.core.domain.team.TeamCommandService;
import com.yellobook.core.domain.team.mapper.ParticipantMapper;
import com.yellobook.core.domain.team.mapper.TeamMapper;
import com.yellobook.member.entity.Member;
import com.yellobook.member.repository.MemberRepository;
import com.yellobook.support.error.code.TeamErrorCode;
import com.yellobook.support.error.exception.CustomException;
import com.yellobook.team.ParticipantEntity;
import com.yellobook.team.dto.request.CreateTeamRequest;
import com.yellobook.team.dto.response.CreateTeamResponse;
import com.yellobook.team.dto.response.JoinTeamResponse;
import com.yellobook.team.entity.Team;
import com.yellobook.team.repository.ParticipantRepository;
import com.yellobook.team.repository.TeamRepository;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
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
public class TeamCommandServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private ParticipantMapper participantMapper;


    private CustomOAuth2User customOAuth2User;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamCommandService teamCommandService;

    private Member member;
    private Team team;
    private ParticipantEntity participantEntity;

    @BeforeEach
    void setUp() {
        member = createMember(1L);
        team = createTeam(1L);
        participantEntity = createParticipant(1L, team, member, TeamMemberRole.ADMIN);

        OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
        customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

    }

    private Member createMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .build();
    }

    private Team createTeam(Long teamId) {
        Team team = Team.builder()
                .name("test")
                .phoneNumber("010")
                .address("seoul")
                .build();
        try {
            Field idField = Team.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(team, teamId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set team ID", e);
        }

        return team;
    }

    private ParticipantEntity createParticipant(Long participantId, Team team, Member member, TeamMemberRole role) {
        ParticipantEntity participantEntity = ParticipantEntity.builder()
                .team(team)
                .member(member)
                .teamMemberRole(role)
                .build();
        try {
            Field idField = ParticipantEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(participantEntity, participantId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set participant ID", e);
        }

        return participantEntity;
    }

    @Nested
    @DisplayName("createTeam 메소드는")
    class Describe_createTeam {
        @Nested
        @DisplayName("중복된 이름의 팀을 생성하는 요청을 받은 경우")
        class Context_InvalidName {

            CreateTeamRequest duplicateNameRequest;
            CustomException exception;

            @BeforeEach
            void setUp() {
                duplicateNameRequest = new CreateTeamRequest(
                        "duplicate team name",
                        "01000000000",
                        "seoul",
                        TeamMemberRole.ADMIN);

                when(teamRepository.findByName(duplicateNameRequest.name())).thenReturn(Optional.of(team));
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

                exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.createTeam(duplicateNameRequest, customOAuth2User.getMemberId());
                });
            }

            @Test
            @DisplayName("EXIST_TEAM_NAME 에러를 반환한다.")
            void it_cannot_create() {
                assertEquals(TeamErrorCode.EXIST_TEAM_NAME, exception.getErrorCode());
                verify(teamRepository).findByName(duplicateNameRequest.name());
            }
        }

        @Nested
        @DisplayName("중복되지 않은 이름의 팀을 생성하는 요청일 경우")
        class Context_ValidName {

            CreateTeamRequest validRequest;
            CreateTeamResponse response;

            @BeforeEach
            void setUp() {
                validRequest = new CreateTeamRequest(
                        "nike",
                        "01000000000",
                        "seoul",
                        TeamMemberRole.ADMIN);

                when(teamRepository.findByName(validRequest.name())).thenReturn(Optional.empty());
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(participantMapper.toParticipant(validRequest.role(), team, member)).thenReturn(participantEntity);
                when(teamMapper.toTeam(validRequest)).thenReturn(team);
                when(teamMapper.toCreateTeamResponse(any(Team.class))).thenReturn(
                        new CreateTeamResponse(1L, LocalDateTime.now()));

                response = teamCommandService.createTeam(validRequest, customOAuth2User.getMemberId());
            }

            @Test
            @DisplayName("정상적으로 생성하고 현재 팀 위치를 생성한 팀으로 변경한다.")
            void it_can_create() {
                verify(teamRepository).findByName(validRequest.name());
                verify(teamRepository).save(team);
                verify(participantRepository).save(participantEntity);
                verify(teamService).setMemberCurrentTeam(member.getId(), team.getId(), validRequest.role()
                        .name());

                assertNotNull(response);
                assertEquals(1L, response.teamId());
            }
        }
    }

    @Nested
    @DisplayName("joinTeam 메소드는")
    class Describe_joinTeam {

        @Nested
        @DisplayName("가입되지 않은 멤버가 접근을 한 경우")
        class Context_Not_Exist_Member {

            String code;
            InvitationResponse invitationResponse;
            CustomException exception;

            @BeforeEach
            void setUp() {
                code = "invitationCode";
                invitationResponse = new InvitationResponse(team.getId(), TeamMemberRole.ORDERER);
                when(teamService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

                exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User.getMemberId(), code);
                });
            }

            @Test
            @DisplayName("MEMBER_NOT_FOUND 에러를 반환한다.")
            void it_returns_MemberNotFound() {
                assertEquals(TeamErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
                verify(participantRepository, never()).save(any(ParticipantEntity.class));
            }
        }

        @Nested
        @DisplayName("Admin이 존재하는 팀에 Admin 초대 코드인 경우")
        class Context_Admin_Exist_And_Join_As_Admin {

            String adminInvitationCode;
            InvitationResponse invitationResponse;

            CustomException exception;

            @BeforeEach
            void setUp() {
                adminInvitationCode = "adminInvitationCode";
                invitationResponse = new InvitationResponse(team.getId(), TeamMemberRole.ADMIN);

                when(teamService.getInvitationInfo(adminInvitationCode)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndTeamMemberRole(team.getId(),
                        TeamMemberRole.ADMIN)).thenReturn(
                        Optional.of(participantEntity));

                exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User.getMemberId(), adminInvitationCode);
                });
            }

            @Test
            @DisplayName("ADMIN_ALREADY_EXIST 에러를 반환한다.")
            void it_returns_AdminAlreadyExist() {
                assertEquals(TeamErrorCode.ADMIN_EXISTS, exception.getErrorCode());
                verify(participantRepository, never()).save(any(ParticipantEntity.class));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 팀에 대한 초대코드일 경우")
        class Context_Team_Not_Exist {

            String notExistTeamCode;
            InvitationResponse invitationResponse;
            CustomException exception;

            @BeforeEach
            void setUp() {
                notExistTeamCode = "invitationCode";
                invitationResponse = new InvitationResponse(team.getId(), TeamMemberRole.ORDERER);

                when(teamService.getInvitationInfo(notExistTeamCode)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

                exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User.getMemberId(), notExistTeamCode);
                });
            }

            @Test
            @DisplayName("TEAM_NOT_FOUND 에러를 반환한다.")
            void it_returns_TeamNotFound() {
                assertEquals(TeamErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
                verify(participantRepository, never()).save(any(ParticipantEntity.class));
            }
        }

        @Nested
        @DisplayName("해당 팀에 이미 가입된 멤버인 경우")
        class Context_Member_Already_In_Team {

            Long belongTeamId;
            String belongTeamCode;
            InvitationResponse invitationResponse;
            CustomException exception;

            @BeforeEach
            void setUp() {
                belongTeamId = team.getId();
                belongTeamCode = "invitationCode";
                invitationResponse = new InvitationResponse(belongTeamId, TeamMemberRole.ADMIN);

                when(teamService.getInvitationInfo(belongTeamCode)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndMemberId(belongTeamId, 1L)).thenReturn(
                        Optional.of(participantEntity));

                exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User.getMemberId(), belongTeamCode);
                });
            }

            @Test
            @DisplayName("MEMBER_ALREADY_EXIST 에러를 반환한다.")
            void it_returns_MemberAlreadyExist() {
                assertEquals(TeamErrorCode.MEMBER_ALREADY_EXIST, exception.getErrorCode());
                verify(participantRepository, never()).save(any(ParticipantEntity.class));
            }
        }

        @Nested
        @DisplayName("존재하는 멤버이지만, 팀에 가입되지 않은 경우")
        class Context_Valid {

            String code;
            InvitationResponse invitationResponse;
            JoinTeamResponse response;

            @BeforeEach
            void setUp() {
                code = "validInvitationCode";
                invitationResponse = new InvitationResponse(team.getId(), TeamMemberRole.ADMIN);
                when(teamService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(
                        Optional.empty());
                when(participantMapper.toParticipant(TeamMemberRole.ADMIN, team, member)).thenReturn(participantEntity);
                when(teamMapper.toJoinTeamResponse(team)).thenReturn(new JoinTeamResponse(team.getId()));

                response = teamCommandService.joinTeam(customOAuth2User.getMemberId(), code);
            }

            @Test
            @DisplayName("팀에 가입시키고 현재 팀을 가입한 팀으로 변경한다.")
            void it_returns_member_to_join_team() {
                verify(participantRepository).save(participantEntity);
                verify(teamService).setMemberCurrentTeam(member.getId(), team.getId(), TeamMemberRole.ADMIN.name());

                assertNotNull(response);
                assertNotNull(response.teamId());
            }
        }
    }

    @Nested
    @DisplayName("leaveTeam 메소드는")
    class Describe_leaveTeam {

        @Nested
        @DisplayName("팀에 속한 멤버가 아닌 경우")
        class Context_Not_Team_Member {

            Long teamId;
            Long memberId;
            CustomException exception;

            @BeforeEach
            void setUp() {
                member = createMember(3L);
                team = createTeam(1L);

                teamId = team.getId();
                memberId = member.getId();

                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

                when(participantRepository.findByTeamIdAndMemberId(teamId, memberId)).thenReturn(Optional.empty());

                exception = assertThrows(
                        CustomException.class, () -> teamCommandService.leaveTeam(teamId, member.getId()));
            }

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_UserNotInThatTeam() {
                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
                verify(participantRepository, never()).delete(any(ParticipantEntity.class));
            }
        }

        @Nested
        @DisplayName("팀 멤버인 경우")
        class Context_Team_Member {

            Long teamId;
            Long memberId;

            @BeforeEach
            void setUp() {
                teamId = team.getId();
                memberId = member.getId();

                when(participantRepository.findByTeamIdAndMemberId(teamId, memberId)).thenReturn(
                        Optional.of(participantEntity));
                when(participantRepository.findFirstByMemberIdOrderByCreatedAtAsc(memberId)).thenReturn(
                        Optional.of(participantEntity));

                teamCommandService.leaveTeam(teamId, memberId);
            }

            @Test
            @DisplayName("정상적으로 팀에서 나가고 현재 팀을 가장 최근 팀으로 변경한다.")
            void it_returns_member_to_leave_team() {
                verify(participantRepository).delete(participantEntity); // 참가자 삭제 확인
                verify(teamService, times(1)).setMemberCurrentTeam(anyLong(), anyLong(), anyString());
            }
        }
    }
}
