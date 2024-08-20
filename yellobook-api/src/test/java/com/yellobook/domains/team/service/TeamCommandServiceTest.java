package com.yellobook.domains.team.service;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domains.auth.dto.InvitationResponse;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.dto.request.CreateTeamRequest;
import com.yellobook.domains.team.dto.response.CreateTeamResponse;
import com.yellobook.domains.team.dto.response.JoinTeamResponse;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.mapper.ParticipantMapper;
import com.yellobook.domains.team.mapper.TeamMapper;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private RedisTeamService redisService;

    @InjectMocks
    private TeamCommandService teamCommandService;

    private Member member;
    private Team team;
    private Participant participant;

    @BeforeEach
    void setUp() {
        member = createMember(1L);
        team = createTeam(1L);
        participant = createParticipant(1L, team, member, MemberTeamRole.ADMIN);

        OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
        customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

    }
    @Nested
    @DisplayName("createTeam 메소드는")
    class Describe_createTeam {
        @Nested
        @DisplayName("중복된 이름을 생성할 경우")
        class Context_InvalidName{
            @Test
            @DisplayName("EXIST_TEAM_NAME 에러를 반환한다.")
            void it_cannot_create(){
                //given
                CreateTeamRequest request = new CreateTeamRequest(
                        "nike",
                        "01000000000",
                        "seoul",
                        MemberTeamRole.ADMIN);

                when(teamRepository.findByName(request.name())).thenReturn(Optional.of(team));
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

                //when
                CustomException exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.createTeam(request, customOAuth2User);
                });
                //then
                assertEquals(TeamErrorCode.EXIST_TEAM_NAME, exception.getErrorCode());
                verify(teamRepository).findByName(request.name());

            }
        }
        @Nested
        @DisplayName("중복되지 않은 이름일 경우")
        class Context_ValidName{
            @Test
            @DisplayName("정상적으로 생성된다.")
            void it_can_create(){
                //given
                CreateTeamRequest request = new CreateTeamRequest(
                        "nike",
                        "01000000000",
                        "seoul",
                        MemberTeamRole.ADMIN);

                when(teamRepository.findByName(request.name())).thenReturn(Optional.empty());
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(participantMapper.toParticipant(request.role(), team, member)).thenReturn(participant);
                when(teamMapper.toTeam(request)).thenReturn(team);
                when(teamMapper.toCreateTeamResponse(any(Team.class))).thenReturn(new CreateTeamResponse(1L, LocalDateTime.now()));

                //when
                CreateTeamResponse response = teamCommandService.createTeam(request, customOAuth2User);

                //then
                verify(teamRepository).findByName(request.name());
                verify(teamRepository).save(team);
                verify(participantRepository).save(participant);
                verify(redisService).setMemberCurrentTeam(member.getId(), team.getId(), request.role().name());

                assertNotNull(response);
                assertEquals(1L, response.teamId());
            }
        }
    }

    @Nested
    @DisplayName("joinTeam 메소드는")
    class Describe_joinTeam {

        @Nested
        @DisplayName("멤버가 존재하지 않을 경우")
        class Context_Not_Exist_Member{

            @Test
            @DisplayName("MEMBER_NOT_FOUND 에러를 반환한다.")
            void it_returns_MemberNotFound(){
                //given
                String code = "invitationCode";
                InvitationResponse invitationResponse = new InvitationResponse(1L, MemberTeamRole.ORDERER);
                when(redisService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

                //when
                CustomException exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User, code);
                });

                //then
                assertEquals(TeamErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("Admin이 존재하는 경우 Admin으로 참가하고자 하면")
        class Context_Admin_Exist_And_Join_As_Admin{

            @Test
            @DisplayName("ADMIN_ALREADY_EXIST 에러를 반환한다.")
            void it_returns_AdminAlreadyExist(){
                //given
                String code = "invitationCode";
                InvitationResponse invitationResponse = new InvitationResponse(1L, MemberTeamRole.ADMIN);

                when(redisService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.of(participant));

                //when
                CustomException exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User, code);
                });

                //then
                assertEquals(TeamErrorCode.ADMIN_EXISTS, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("Team이 존재하지 않는 경우")
        class Context_Team_Not_Exist{

            @Test
            @DisplayName("TEAM_NOT_FOUND 에러를 반환한다.")
            void it_returns_TeamNotFound(){
                //given
                String code = "invitationCode";
                InvitationResponse invitationResponse = new InvitationResponse(1L, MemberTeamRole.ORDERER);

                when(redisService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

                //when
                CustomException exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User, code);
                });

                //then
                assertEquals(TeamErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("이미 가입된 멤버인 경우")
        class Context_Member_Already_In_Team{

            @Test
            @DisplayName("MEMBER_ALREADY_EXIST 에러를 반환한다.")
            void it_returns_MemberAlreadyExist(){
                //given
                Long teamId = 1L;
                String code = "invitationCode";
                InvitationResponse invitationResponse = new InvitationResponse(1L, MemberTeamRole.ADMIN);

                when(redisService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
                when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndMemberId(teamId, 1L)).thenReturn(Optional.of(participant));

                //when
                CustomException exception = assertThrows(CustomException.class, () -> {
                    teamCommandService.joinTeam(customOAuth2User, code);
                });

                //then
                assertEquals(TeamErrorCode.MEMBER_ALREADY_EXIST, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("정상적인 요청일 경우")
        class Context_Valid{

            @Test
            @DisplayName("팀에 가입시킨다.")
            void it_returns_member_to_join_team(){
                //given
                String code = "invitationCode";
                InvitationResponse invitationResponse = new InvitationResponse(team.getId(), MemberTeamRole.ADMIN);

                when(redisService.getInvitationInfo(code)).thenReturn(invitationResponse);
                when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(Optional.empty());
                when(participantMapper.toParticipant(MemberTeamRole.ADMIN, team, member)).thenReturn(participant);
                when(teamMapper.toJoinTeamResponse(team)).thenReturn(new JoinTeamResponse(team.getId()));

                //when
                JoinTeamResponse response = teamCommandService.joinTeam(customOAuth2User, code);

                //then
                verify(participantRepository).save(participant);
                verify(redisService).setMemberCurrentTeam(member.getId(), team.getId(), MemberTeamRole.ADMIN.name());

                assertNotNull(response);
                assertNotNull(response.teamId());
            }
        }
    }

    @Nested
    @DisplayName("leaveTeam 메소드는")
    class Describe_leaveTeam{

        @Nested
        @DisplayName("팀 멤버가 아닌 경우")
        class Context_Not_Team_Member{

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_UserNotInThatTeam(){
                //given
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(Optional.empty());

                //when
                CustomException exception = assertThrows(
                        CustomException.class, () -> teamCommandService.leaveTeam(team.getId(), customOAuth2User));

                //then
                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
                verify(participantRepository, never()).delete(any(Participant.class));
            }
        }

        @Nested
        @DisplayName("팀 멤버인 경우")
        class Context_Team_Member{

            @Test
            @DisplayName("정상적으로 팀에서 나간다.")
            void it_returns_member_to_leave_team(){
                //given
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(Optional.of(participant));
                when(participantRepository.findFirstByMemberIdOrderByCreatedAtAsc(member.getId())).thenReturn(Optional.of(participant));

                //when
                teamCommandService.leaveTeam(team.getId(), customOAuth2User);

                //then
                verify(participantRepository).delete(participant); // 참가자 삭제 확인
                verify(redisService, times(1)).setMemberCurrentTeam(anyLong(), anyLong(), anyString());
            }
        }
    }

    private Member createMember(Long memberId){
        return Member.builder().memberId(memberId).build();
    }

    private Team createTeam(Long teamId){
        Team team = Team.builder().name("test").phoneNumber("010").address("seoul").build();
        try {
            Field idField = Team.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(team, teamId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set team ID", e);
        }

        return team;
    }

    private Participant createParticipant(Long participantId, Team team, Member member, MemberTeamRole role){
        Participant participant = Participant.builder().team(team).member(member).role(role).build();
        try {
            Field idField = Participant.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(participant, participantId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set participant ID", e);
        }

        return participant;
    }
}
