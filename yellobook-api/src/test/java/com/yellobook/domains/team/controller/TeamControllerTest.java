package com.yellobook.domains.team.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.MentionDTO;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.InvitationCodeRequest;
import com.yellobook.domains.team.dto.request.CreateTeamRequest;
import com.yellobook.domains.team.dto.response.*;
import com.yellobook.domains.team.service.TeamCommandService;
import com.yellobook.domains.team.service.TeamQueryService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Mock
    private CustomOAuth2User customOAuth2User;

    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;


    private final TeamMemberVO teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

    @BeforeEach
    void setUp() throws Exception {
        Member member = Member.builder()
                .memberId(1L)
                .nickname("test")
                .email("test@example.com")
                .profileImage("profileImage.png")
                .allowance(true)
                .role(MemberRole.USER)
                .build();

        OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);

        customOAuth2User = new CustomOAuth2User(oauth2UserDTO);
    }

    @Test
    @DisplayName("팀 생성")
    public void createTeam_Success() throws Exception {
        // Given
        CreateTeamRequest request = new CreateTeamRequest("nike", "01000000000", "경기도", MemberTeamRole.ADMIN);
        CreateTeamResponse response = new CreateTeamResponse(1L, LocalDateTime.now());

        when(teamCommandService.createTeam(any(CreateTeamRequest.class), any(CustomOAuth2User.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("팀 불러오기")
    public void getTeamTest() throws Exception {
        //given
        Long teamId = 1L;
        GetTeamResponse response = GetTeamResponse.builder().teamId(1L).name("나이키").phoneNumber("01000000000").address("경기도").build();

        when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
        when(teamQueryService.findByTeamId(any(), any())).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/teams/{teamId}", teamId)
                        .content(objectMapper.writeValueAsString(response))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.teamId", CoreMatchers.is(response.teamId().intValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("@ExistTeam - 해당 팀이 존재하지 않을 때 예외 발생")
    public void validTeamTest() throws Exception {
        //given
        Long teamId = 1L;
        when(teamQueryService.existsByTeamId(teamId)).thenReturn(false);

        //when & then
        mockMvc.perform(get("/api/v1/teams/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Nested
    @DisplayName("팀 내의 멤버 검색")
    class TeamMemberSearch{
        @Test
        @DisplayName("모든 멤버")
        void searchMembers_everyone() throws Exception {
            // given
            TeamMemberListResponse response = new TeamMemberListResponse(
                    List.of(new QueryTeamMember(1L,"test1"),
                            new QueryTeamMember(2L, "test2")));

            when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
            when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                    .thenReturn(teamMemberVO);

            when(teamQueryService.findTeamMembers(1L)).thenReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/teams/members")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.members.size()").value(2))
                    .andDo(print());
        }

        @Test
        @DisplayName("특정 접두사")
        void searchMembers_withPrefix() throws Exception {
            // given
            String name = "@john";
            MentionDTO mentionDTO = new MentionDTO(List.of(2L));

            when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
            when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                    .thenReturn(teamMemberVO);

            when(teamQueryService.searchParticipants(teamMemberVO, name)).thenReturn(mentionDTO);

            // when & then
            mockMvc.perform(get("/api/v1/teams/members/search")
                            .param("name", name)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.ids[0]", CoreMatchers.is(2)))
                    .andDo(print());
        }
    }

    @Test
    @DisplayName("팀 초대하기")
    public void inviteTeamTest() throws Exception {
        // given
        Long teamId = 1L;
        String expectedInviteUrl = "https://example.com/invite";
        InvitationCodeResponse response = InvitationCodeResponse.builder()
                .inviteUrl(expectedInviteUrl)
                .build();

        InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ADMIN);
        when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
        when(teamQueryService.makeInvitationCode(any(), any(), any())).thenReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/teams/{teamId}/invite", teamId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.inviteUrl", equalTo(response.inviteUrl())));
    }

    @Test
    @DisplayName("팀 참가하기")
    public void joinTeamTest() throws Exception {
        //given
        Long teamId = 1L;
        String code = "team:code";
        JoinTeamResponse response = new JoinTeamResponse(1L);
        when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
        when(teamCommandService.joinTeam(any(), any())).thenReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/teams/invitation")
                        .param("code", code)
                        .requestAttr("customOAuth2User", customOAuth2User)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.teamId", CoreMatchers.is(1)));
    }

    @Test
    @DisplayName("팀 나가기")
    public void leaveTeamTest() throws Exception {
        //given
        Long teamId = 1L;
        when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
        doNothing().when(teamCommandService).leaveTeam(teamId, customOAuth2User);

        // when & then
        mockMvc.perform(delete("/api/v1/teams/{teamId}/leave", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
