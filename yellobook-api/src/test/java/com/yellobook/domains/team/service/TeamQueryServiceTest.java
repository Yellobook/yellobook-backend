package com.yellobook.domains.team.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.InvitationCodeRequest;
import com.yellobook.domains.team.dto.response.InvitationCodeResponse;
import com.yellobook.domains.team.dto.response.TeamMemberListResponse;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.mapper.TeamMapper;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamQueryServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private RedisTeamService redisService;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamQueryService teamQueryService;

    private CustomOAuth2User customOAuth2User;

    private Member member;
    private Team team;
    private Participant participant;


    @BeforeEach
    void setUp() {
        member = createMember(1L);
        team = createTeam(1L);
        participant = createParticipant(1L, team, member, MemberTeamRole.ORDERER);

        OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
        customOAuth2User = new CustomOAuth2User(oauth2UserDTO);
    }

    @Nested
    @DisplayName("makeInvitationCode 메소드는")
    class Describe_makeInvitationCode {

        @Nested
        @DisplayName("멤버가 팀에 속하지 않은 경우")
        class Context_Member_Not_Belong_Team{

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_cannot_invite(){
                //given
                InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ORDERER);

                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(Optional.empty());

                //when & then
                CustomException exception = assertThrows(
                        CustomException.class, () -> teamQueryService.makeInvitationCode(team.getId(), request, customOAuth2User));

                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("ADMIN이 이미 있을 때, ADMIN을 초대하는 경우")
        class Context_Admin_Already_Exists{

            @Test
            @DisplayName("ADMIN_EXISTS 에러를 반환한다.")
            void it_returns_admin_exists(){
                //given
                Long teamId = 1L;
                Long memberId = 1L;
                Long memberId2 = 2L;
                Member member2 = createMember(memberId2);
                Participant adminPar = createParticipant(2L, team, member2, MemberTeamRole.ADMIN);
                InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ADMIN);

                when(participantRepository.findByTeamIdAndMemberId(teamId, memberId))
                        .thenReturn(Optional.of(participant));
                when(participantRepository.findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN))
                        .thenReturn(Optional.of(adminPar));

                CustomException exception = assertThrows(
                        CustomException.class, () -> teamQueryService.makeInvitationCode(teamId, request, customOAuth2User));

                assertEquals(TeamErrorCode.ADMIN_EXISTS, exception.getErrorCode());
                verify(participantRepository).findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN);
            }
        }

        @Nested
        @DisplayName("문제가 없는 경우")
        class Context_No_Problem{

            @Test
            @DisplayName("초대 코드를 발급한다.")
            void it_returns_invitation_code(){
                //given
                InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ORDERER);

                when(participantRepository.findByTeamIdAndMemberId(team.getId(), customOAuth2User.getMemberId()))
                        .thenReturn(Optional.of(participant)); // 참가자 정보
                when(redisService.generateInvitationUrl(team.getId(), request.role()))
                        .thenReturn("http://invitation-url"); // 초대 URL 생성
                when(teamMapper.toInvitationCodeResponse(anyString()))
                        .thenReturn(new InvitationCodeResponse("http://invitation-url")); // 응답 매핑

                // When
                InvitationCodeResponse response = teamQueryService.makeInvitationCode(team.getId(), request, customOAuth2User);

                // Then
                assertNotNull(response);
                assertEquals("http://invitation-url", response.inviteUrl());
                verify(redisService).generateInvitationUrl(team.getId(), request.role());
            }
        }
    }

    @Nested
    @DisplayName("findTeamMembers 메소드는")
    class Describe_findTeamMembers {
        @Nested
        @DisplayName("팀원이 존재하는 경우")
        class Context_TeamMember{
            @Test
            @DisplayName("모든 팀원을 반환한다.")
            void it_returns_all_team_members(){
                //given
                TeamMemberListResponse response = new TeamMemberListResponse(List.of(
                        new QueryTeamMember(1L, "test1"),
                        new QueryTeamMember(2L, "test2")));

                when(teamRepository.findTeamMembers(team.getId())).thenReturn(response.members());
                when(teamMapper.toTeamMemberListResponse(response.members())).thenReturn(response);
                //when
                TeamMemberListResponse res = teamQueryService.findTeamMembers(team.getId());

                //then
                assertNotNull(res);
                assertEquals(response, res);
            }
        }
        @Nested
        @DisplayName("팀원이 존재하지 않는 경우")
        class Context_No_TeamMember_Exist{

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_returns_empty_list(){
                TeamMemberListResponse response = new TeamMemberListResponse(Collections.emptyList());
                when(teamRepository.findTeamMembers(team.getId())).thenReturn(response.members());
                when(teamMapper.toTeamMemberListResponse(Collections.emptyList())).thenReturn(response);


                TeamMemberListResponse res = teamQueryService.findTeamMembers(team.getId());

                assertEquals(response, res);
                assertNotNull(res);
            }
        }
    }

    @Nested
    @DisplayName("searchParticipants 메소드는")
    class Describe_searchParticipants {
        @Nested
        @DisplayName("TeamMember이면서 검색어로 시작하는 팀원이 존재하는 경우")
        class Context_TeamMember_And_Exist_Team_Members_Start_With_Prefix {
            @Test
            @DisplayName("조건에 맞는 팀원들을 반환한다.")
            void it_returns_team_members_match_the_condition(){
                //given
                String prefix = "test";
                TeamMemberListResponse response = new TeamMemberListResponse(List.of(
                        new QueryTeamMember(1L, "test1"),
                        new QueryTeamMember(2L, "test2")));
                TeamMemberVO teamMember = TeamMemberVO.of(member.getId(),team.getId(), MemberTeamRole.ORDERER);

                when(participantRepository.findMentionsByNamePrefix(prefix, team.getId())).thenReturn(response.members());
                when(teamMapper.toTeamMemberListResponse(response.members())).thenReturn(response);

                //when
                TeamMemberListResponse res = teamQueryService.searchParticipants(teamMember, prefix);

                //then
                assertNotNull(res);
                assertEquals(response, res);
            }
        }
        @Nested
        @DisplayName("TeamMember이지만 검색어로 시작하는 팀원이 없는 경우")
        class Context_TeamMember_But_Not_Exist_Team_Members_Start_With_Prefix {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_returns_empty_list(){
                String prefix = "test";

                TeamMemberListResponse response = new TeamMemberListResponse(Collections.emptyList());
                when(participantRepository.findMentionsByNamePrefix(prefix, team.getId())).thenReturn(response.members());
                when(teamMapper.toTeamMemberListResponse(Collections.emptyList())).thenReturn(response);

                TeamMemberVO teamMember = TeamMemberVO.of(member.getId(),team.getId(), MemberTeamRole.ORDERER);

                TeamMemberListResponse res = teamQueryService.searchParticipants(teamMember, prefix);
                assertNotNull(res);
                assertEquals(response, res);
            }
        }

        @Nested
        @DisplayName("팀에 속하지 않은 경우")
        class Context_Not_TeamMember{

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_user_not_in_that_team(){
                //given
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), customOAuth2User.getMemberId())).thenReturn(Optional.empty());

                CustomException exception = assertThrows(
                        CustomException.class, () -> teamQueryService.findByTeamId(team.getId(), customOAuth2User));

                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("existsByTeamId 메소드는")
    class Describe_existsByTeamId {

        @Nested
        @DisplayName("Team이 존재하는 경우")
        class Context_Exist_Team{

            @Test
            @DisplayName("true를 반환한다.")
            void it_returns_true(){
                //given
                when(teamRepository.existsById(team.getId())).thenReturn(true);

                //when
                boolean result = teamQueryService.existsByTeamId(team.getId());

                //then
                assertTrue(result);
            }
        }

        @Nested
        @DisplayName("Team이 존재하지 않는 경우")
        class Context_Not_Exist_Team{

            @Test
            @DisplayName("false를 반환한다.")
            void it_returns_false(){
                //given
                when(teamRepository.existsById(team.getId())).thenReturn(false);

                //when
                boolean result = teamQueryService.existsByTeamId(team.getId());

                //then
                assertFalse(result);
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
