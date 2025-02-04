package com.yellobook.domains.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.member.dto.response.CurrentTeamResponse;
import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.service.MemberQueryService;
import java.util.Collections;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberController Unit Test")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberQueryService memberQueryService;

    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    private Authentication authentication;

    @Nested
    @DisplayName("getMemberProfile 메소드는")
    class Describe_GetMemberProfile {
        @Nested
        @DisplayName("인증된 사용자가 프로필을 요청하면")
        class Context_member_exist {
            ProfileResponse response;

            @BeforeEach
            void setUpContext() {
                Member member = Member.builder()
                        .memberId(1L)
                        .nickname("yellow")
                        .email("email")
                        .profileImage("image")
                        .allowance(true)
                        .role(MemberRole.USER)
                        .build();

                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                CustomOAuth2User user = new CustomOAuth2User(oauth2UserDTO);
                authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                response = new ProfileResponse(1L, "사용자", "profile.png", "example@gmail.com", Collections.emptyList());
                when(memberQueryService.getMemberProfile(any(Long.class))).thenReturn(response);
            }

            @Test
            @DisplayName("프로필 정보를 반환한다.")
            void it_returns_profile() throws Exception {
                mockMvc.perform(get("/api/v1/members/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(authentication)
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(
                                jsonPath("$.data.nickname", CoreMatchers.is(response.nickname())))
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("getMemberCurrentTeam 메서드는")
    class Describe_getMemberCurrentTeam {
        @Nested
        @DisplayName("팀에 가입한 사용자라면")
        class Context_with_team_member {
            CurrentTeamResponse response;

            @BeforeEach
            void setUpContext() throws Exception {
                response = CurrentTeamResponse.builder()
                        .teamId(1L)
                        .teamName("옐로북 팀")
                        .build();
                TeamMemberVO teamMember = TeamMemberVO.of(1L, 100L, TeamMemberRole.ADMIN);
                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMember);
                when(memberQueryService.getMemberCurrentTeam(any(Long.class))).thenReturn(response);
            }

            @Test
            @DisplayName("현재 사용자가 위치한 팀 Id, 팀 이름을 반환해야 한다.")
            void it_returns_current_team() throws Exception {
                mockMvc.perform(get("/api/v1/members/teams/current")
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(
                                jsonPath("$.data.teamId").value(response.teamId())
                        )
                        .andExpect(
                                jsonPath("$.data.teamName").value(response.teamName())
                        )
                        .andReturn();
            }
        }
    }
}
