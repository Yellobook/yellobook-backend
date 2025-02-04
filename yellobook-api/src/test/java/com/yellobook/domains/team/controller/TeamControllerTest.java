package com.yellobook.domains.team.controller;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.CreateTeamRequest;
import com.yellobook.domains.team.dto.request.InvitationCodeRequest;
import com.yellobook.domains.team.dto.response.CreateTeamResponse;
import com.yellobook.domains.team.dto.response.GetTeamResponse;
import com.yellobook.domains.team.dto.response.InvitationCodeResponse;
import com.yellobook.domains.team.dto.response.JoinTeamResponse;
import com.yellobook.domains.team.dto.response.TeamMemberListResponse;
import com.yellobook.domains.team.service.TeamCommandService;
import com.yellobook.domains.team.service.TeamQueryService;
import com.yellobook.domains.team.util.SecurityUtil;
import fixture.MemberFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TeamControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamCommandService teamCommandService;
    @MockBean
    private TeamQueryService teamQueryService;
    @MockBean
    private RedisTeamService redisTeamService;
    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    private final Long teamId = 1L;
    private final Member member = MemberFixture.createMember();

    @Nested
    @DisplayName("createTeam 메소드는")
    class Describe_createTeam {

        @Nested
        @DisplayName("유효한 요청인 경우")
        class Context_Valid_Request {
            CreateTeamRequest request;
            CreateTeamResponse response;

            CustomOAuth2User customOAuth2User;

            @BeforeEach
            void setUp() {
                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

                SecurityUtil.setAuthentication(customOAuth2User);

                request = new CreateTeamRequest("nike", "01000000000", "경기도", TeamMemberRole.ADMIN);
                response = new CreateTeamResponse(1L, LocalDateTime.now());

                when(teamCommandService.createTeam(request, customOAuth2User.getMemberId()))
                        .thenReturn(response);
            }

            @Test
            @DisplayName("201 Created를 반환한다.")
            void it_returns_201_created() throws Exception {
                mockMvc.perform(post("/api/v1/teams")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                        )
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("getTeam 메소드는")
    class Describe_getTeam {

        @Nested
        @DisplayName("존재하는 팀을 불러오는 경우")
        class Context_Exist_Team {
            CustomOAuth2User customOAuth2User;
            GetTeamResponse response;

            @BeforeEach
            void setUp() {
                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

                SecurityUtil.setAuthentication(customOAuth2User);

                response = GetTeamResponse.builder()
                        .teamId(1L)
                        .name("나이키")
                        .phoneNumber("01000000000")
                        .address("경기도")
                        .build();

                when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
                when(teamQueryService.findByTeamId(teamId, customOAuth2User.getMemberId())).thenReturn(response);
            }

            @Test
            @DisplayName("200 OK, 그리고 해당 팀에 대한 정보를 반환한다.")
            void it_returns_200_ok_and_get_team() throws Exception {
                mockMvc.perform(get("/api/v1/teams/{teamId}", teamId)
                                .content(objectMapper.writeValueAsString(response))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.teamId", CoreMatchers.is(response.teamId()
                                .intValue())))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("getTeamMembers메소드는")
    class Describe_getTeamMembers {

        @Nested
        @DisplayName("팀 내에 멤버가 존재한다면")
        class Context_Exist_TeamMember {
            TeamMemberListResponse response;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                teamMemberVO = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);
                response = new TeamMemberListResponse(
                        List.of(new QueryTeamMember(1L, "test1"),
                                new QueryTeamMember(2L, "test2")));

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(teamQueryService.findTeamMembers(1L)).thenReturn(response);
            }

            @Test
            @DisplayName("200 OK, 그리고 팀에 속하는 모든 멤버를 반환한다.")
            void it_returns_200_ok_and_all_team_members() throws Exception {
                mockMvc.perform(get("/api/v1/teams/members")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.members.size()").value(2))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("searchMembers 메소드는")
    class Describe_searchMembers {

        @Nested
        @DisplayName("특정 접두사로 시작하는 멤버가 존재하면")
        class Context_Exist_TeamMember_Starts_With_Prefix {

            String name;
            TeamMemberListResponse response;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                name = "john";
                response = new TeamMemberListResponse(
                        List.of(new QueryTeamMember(2L, "john")));
                teamMemberVO = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(teamQueryService.searchParticipants(teamMemberVO.getTeamId(), name)).thenReturn(response);
            }

            @Test
            @DisplayName("200 OK, 그리고 해당 멤버들을 리스트로 반환한다.")
            void it_returns_200_ok_and_list_of_matching_members() throws Exception {
                mockMvc.perform(get("/api/v1/teams/members/search")
                                .param("name", name)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.members[0].memberId", CoreMatchers.is(2)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("특정 접두사로 시작하는 멤버가 존재하지 않는다면")
        class Context_Not_Exist_TeamMember_Starts_With_Prefix {

            String name;
            TeamMemberListResponse response;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                name = "john";
                response = new TeamMemberListResponse(List.of());
                teamMemberVO = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(teamQueryService.searchParticipants(teamMemberVO.getTeamId(), name)).thenReturn(response);
            }

            @Test
            @DisplayName("200 OK, 그리고 빈 리스트를 반환한다.")
            void it_returns_200_ok_and_empty_list() throws Exception {
                mockMvc.perform(get("/api/v1/teams/members/search")
                                .param("name", name)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.members.size()").value(0))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("inviteTeam 메소드는")
    class Describe_inviteTeam {

        @Nested
        @DisplayName("정상적인 요청이라면")
        class Context_Valid_Request {
            String expectedInviteUrl;
            InvitationCodeRequest request;
            InvitationCodeResponse response;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                expectedInviteUrl = "https://example.com/invite";
                response = InvitationCodeResponse.builder()
                        .inviteUrl(expectedInviteUrl)
                        .build();
                request = new InvitationCodeRequest(TeamMemberRole.ADMIN);
                teamMemberVO = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(teamMemberVO);

                when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
                when(teamQueryService.makeInvitationCode(any(), any(), any())).thenReturn(response);
            }

            @Test
            @DisplayName("201 Created, 그리고 생성된 초대 코드를 반환한다")
            void it_returns_201_created_and_invitation_code() throws Exception {
                mockMvc.perform(post("/api/v1/teams/{teamId}/invite", teamId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.data.inviteUrl", equalTo(response.inviteUrl())));
            }
        }
    }

    @Nested
    @DisplayName("joinTeam 메소드는")
    class Describe_joinTeam {

        @Nested
        @DisplayName("유효한 초대 코드인 경우")
        class Context_Valid_Invitation_Code {
            String code;
            JoinTeamResponse response;
            CustomOAuth2User customOAuth2User;

            @BeforeEach
            void setUp() {
                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

                SecurityUtil.setAuthentication(customOAuth2User);
                code = "code";
                response = new JoinTeamResponse(1L);

                when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
                when(teamCommandService.joinTeam(any(), any())).thenReturn(response);
            }

            @Test
            @DisplayName("200 Ok, 그리고 팀 id를 반환한다.")
            void it_returns_200_ok_and_team_id() throws Exception {
                mockMvc.perform(post("/api/v1/teams/invitation")
                                .param("code", code)
                                .requestAttr("customOAuth2User", customOAuth2User)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.teamId", CoreMatchers.is(1)));
            }
        }
    }

    @Nested
    @DisplayName("leaveTeam 메소드는")
    class Describe_leaveTeam {

        @Nested
        @DisplayName("팀에 속한 멤버가 요청을 하는 경우")
        class Context_Team_Member {

            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                teamMemberVO = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
                doNothing().when(teamCommandService)
                        .leaveTeam(teamId, teamMemberVO.getMemberId());
            }

            @Test
            @DisplayName("204 NoContent를 반환한다.")
            void it_returns_204_no_content() throws Exception {
                mockMvc.perform(delete("/api/v1/teams/{teamId}/leave", teamId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
