package com.yellobook.team.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yellobook.TeamMemberRole;
import com.yellobook.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.auth.service.TeamService;
import com.yellobook.core.domain.team.TeamMemberVO;
import com.yellobook.member.entity.Member;
import com.yellobook.support.error.code.TeamErrorCode;
import com.yellobook.support.error.exception.CustomException;
import com.yellobook.team.Participant;
import com.yellobook.core.domain.team.TeamQueryService;
import com.yellobook.team.dto.query.QueryTeamMember;
import com.yellobook.team.dto.request.InvitationCodeRequest;
import com.yellobook.team.dto.response.InvitationCodeResponse;
import com.yellobook.team.dto.response.TeamMemberListResponse;
import com.yellobook.team.entity.Team;
import com.yellobook.core.domain.team.mapper.TeamMapper;
import com.yellobook.team.repository.ParticipantRepository;
import com.yellobook.team.repository.TeamRepository;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeamQueryServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private TeamService redisService;

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
        participant = createParticipant(1L, team, member, TeamMemberRole.ORDERER);

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

    private Participant createParticipant(Long participantId, Team team, Member member, TeamMemberRole role) {
        Participant participant = Participant.builder()
                .team(team)
                .member(member)
                .teamMemberRole(role)
                .build();
        try {
            Field idField = Participant.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(participant, participantId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set participant ID", e);
        }

        return participant;
    }

    @Nested
    @DisplayName("makeInvitationCode 메소드는")
    class Describe_makeInvitationCode {

        @Nested
        @DisplayName("멤버가 팀에 속하지 않은 경우")
        class Context_Member_Not_Belong_Team {

            InvitationCodeRequest request;
            CustomException exception;

            @BeforeEach
            void setUp() {
                request = new InvitationCodeRequest(TeamMemberRole.ORDERER);

                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(
                        Optional.empty());

                exception = assertThrows(
                        CustomException.class, () -> teamQueryService.makeInvitationCode(team.getId(), request, 1L));

            }

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_cannot_invite() {
                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("ADMIN이 이미 있을 때, ADMIN을 초대하는 경우")
        class Context_Admin_Already_Exists {
            Long teamId;
            Long memberId;
            Long memberId2;

            Member member2;
            Participant adminPar;
            InvitationCodeRequest request;
            CustomException exception;

            @BeforeEach
            void setUp() {
                teamId = team.getId();
                memberId = member.getId();
                memberId2 = 2L;
                member2 = createMember(memberId2);
                adminPar = createParticipant(2L, team, member2, TeamMemberRole.ADMIN);
                request = new InvitationCodeRequest(TeamMemberRole.ADMIN);

                when(participantRepository.findByTeamIdAndMemberId(teamId, memberId))
                        .thenReturn(Optional.of(participant));
                when(participantRepository.findByTeamIdAndTeamMemberRole(teamId, TeamMemberRole.ADMIN))
                        .thenReturn(Optional.of(adminPar));

                exception = assertThrows(
                        CustomException.class, () -> teamQueryService.makeInvitationCode(teamId, request, memberId));
            }

            @Test
            @DisplayName("ADMIN_EXISTS 에러를 반환한다.")
            void it_returns_admin_exists() {
                assertEquals(TeamErrorCode.ADMIN_EXISTS, exception.getErrorCode());
                verify(participantRepository).findByTeamIdAndTeamMemberRole(teamId, TeamMemberRole.ADMIN);
            }
        }

        @Nested
        @DisplayName("문제가 없는 경우")
        class Context_No_Problem {

            InvitationCodeRequest request;
            InvitationCodeResponse response;

            @BeforeEach
            void setUp() {
                request = new InvitationCodeRequest(TeamMemberRole.ORDERER);

                when(participantRepository.findByTeamIdAndMemberId(team.getId(), customOAuth2User.getMemberId()))
                        .thenReturn(Optional.of(participant)); // 참가자 정보
                when(redisService.generateInvitationUrl(team.getId(), request.role()))
                        .thenReturn("http://invitation-url"); // 초대 URL 생성
                when(teamMapper.toInvitationCodeResponse(anyString()))
                        .thenReturn(new InvitationCodeResponse("http://invitation-url")); // 응답 매핑

                response = teamQueryService.makeInvitationCode(team.getId(), request, member.getId());
            }

            @Test
            @DisplayName("초대 코드를 발급한다.")
            void it_returns_invitation_code() {
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
        class Context_TeamMember {

            TeamMemberListResponse res;

            @BeforeEach
            void setUp() {
                List<QueryTeamMember> members = List.of(
                        new QueryTeamMember(1L, "test1"),
                        new QueryTeamMember(2L, "test2")
                );
                when(teamRepository.findTeamMembers(team.getId())).thenReturn(members);
                when(teamMapper.toTeamMemberListResponse(members)).thenReturn(new TeamMemberListResponse(members));

                //when
                res = teamQueryService.findTeamMembers(team.getId());
            }

            @Test
            @DisplayName("모든 팀원을 반환한다.")
            void it_returns_all_team_members() {
                assertNotNull(res);
                assertEquals(2, res.members()
                        .size());
                Assertions.assertEquals("test1", res.members()
                        .get(0)
                        .nickname());
                Assertions.assertEquals("test2", res.members()
                        .get(1)
                        .nickname());
            }
        }

        @Nested
        @DisplayName("팀원이 존재하지 않는 경우")
        class Context_No_TeamMember_Exist {

            TeamMemberListResponse res;

            @BeforeEach
            void setUp() {
                List<QueryTeamMember> members = List.of();

                when(teamRepository.findTeamMembers(team.getId())).thenReturn(members);
                when(teamMapper.toTeamMemberListResponse(Collections.emptyList())).thenReturn(
                        new TeamMemberListResponse(members));

                res = teamQueryService.findTeamMembers(team.getId());
            }

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_returns_empty_list() {
                assertNotNull(res);
                assertEquals(0, res.members()
                        .size());
            }
        }
    }

    @Nested
    @DisplayName("searchParticipants 메소드는")
    class Describe_searchParticipants {

        @Nested
        @DisplayName("TeamMember이면서 검색어로 시작하는 팀원이 존재하는 경우")
        class Context_TeamMember_And_Exist_Team_Members_Start_With_Prefix {

            String prefix;
            TeamMemberListResponse res;
            TeamMemberVO teamMember;

            @BeforeEach
            void setUp() {
                prefix = "test";
                List<QueryTeamMember> members = List.of(
                        new QueryTeamMember(1L, "test1"),
                        new QueryTeamMember(2L, "test2")
                );
                teamMember = TeamMemberVO.of(member.getId(), team.getId(), TeamMemberRole.ORDERER);

                when(participantRepository.findMentionsByNamePrefix(prefix, team.getId())).thenReturn(members);
                when(teamMapper.toTeamMemberListResponse(members)).thenReturn(new TeamMemberListResponse(members));

                res = teamQueryService.searchParticipants(teamMember.getTeamId(), prefix);
            }

            @Test
            @DisplayName("조건에 맞는 팀원들을 반환한다.")
            void it_returns_team_members_match_the_condition() {
                assertNotNull(res);
                assertEquals(2, res.members()
                        .size());
                Assertions.assertEquals("test1", res.members()
                        .get(0)
                        .nickname());
                Assertions.assertEquals("test2", res.members()
                        .get(1)
                        .nickname());
            }
        }

        @Nested
        @DisplayName("TeamMember이지만 검색어로 시작하는 팀원이 없는 경우")
        class Context_TeamMember_But_Not_Exist_Team_Members_Start_With_Prefix {

            String prefix;
            TeamMemberListResponse res;
            TeamMemberVO teamMember;

            @BeforeEach
            void setUp() {
                prefix = "test";
                List<QueryTeamMember> members = List.of();
                teamMember = TeamMemberVO.of(member.getId(), team.getId(), TeamMemberRole.ORDERER);
                when(participantRepository.findMentionsByNamePrefix(prefix, team.getId())).thenReturn(members);
                when(teamMapper.toTeamMemberListResponse(members)).thenReturn(new TeamMemberListResponse(members));

                res = teamQueryService.searchParticipants(teamMember.getTeamId(), prefix);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_returns_empty_list() {
                assertNotNull(res);
                assertEquals(0, res.members()
                        .size());
            }
        }

        @Nested
        @DisplayName("팀에 속하지 않은 경우")
        class Context_Not_TeamMember {

            CustomException exception;

            @BeforeEach
            void setUp() {
                when(participantRepository.findByTeamIdAndMemberId(
                        team.getId(),
                        customOAuth2User.getMemberId()))
                        .thenReturn(Optional.empty());

                exception = assertThrows(
                        CustomException.class,
                        () -> teamQueryService.findByTeamId(team.getId(), customOAuth2User.getMemberId()));
            }

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_user_not_in_that_team() {
                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("existsByTeamId 메소드는")
    class Describe_existsByTeamId {

        @Nested
        @DisplayName("Team이 존재하는 경우")
        class Context_Exist_Team {

            boolean result;

            @BeforeEach
            void setUp() {
                when(teamRepository.existsById(team.getId())).thenReturn(true);

                result = teamQueryService.existsByTeamId(team.getId());
            }

            @Test
            @DisplayName("true를 반환한다.")
            void it_returns_true() {
                assertTrue(result);
            }
        }

        @Nested
        @DisplayName("Team이 존재하지 않는 경우")
        class Context_Not_Exist_Team {
            boolean result;

            @BeforeEach
            void setUp() {
                when(teamRepository.existsById(team.getId())).thenReturn(false);

                result = teamQueryService.existsByTeamId(team.getId());
            }

            @Test
            @DisplayName("false를 반환한다.")
            void it_returns_false() {
                assertFalse(result);
            }
        }
    }
}
