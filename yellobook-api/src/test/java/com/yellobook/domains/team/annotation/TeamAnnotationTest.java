package com.yellobook.domains.team.annotation;

import static fixture.MemberFixture.createMember;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.controller.TeamController;
import com.yellobook.domains.team.service.TeamCommandService;
import com.yellobook.domains.team.service.TeamQueryService;
import com.yellobook.domains.team.util.SecurityUtil;
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
public class TeamAnnotationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamCommandService teamCommandService;
    @MockBean
    private TeamQueryService teamQueryService;
    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    @Nested
    @DisplayName("ExistTeam 애노테이션은")
    class Describe_ExistTeam_Annotation {

        @Nested
        @DisplayName("해당 팀이 존재하지 않은 경우")
        class Context_Not_Exist_Team {

            Long teamId;

            @BeforeEach
            void setUp() {
                teamId = 1L;

                when(teamQueryService.existsByTeamId(teamId)).thenReturn(false);
            }

            @Test
            @DisplayName("400 Bad Request를 반환한다.")
            void it_returns_400_bad_request() throws Exception {
                mockMvc.perform(get("/api/v1/teams/{teamId}", teamId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("해당 팀이 존재하는 경우")
        class Context_Exist_Team {
            Long teamId;
            Member member;
            CustomOAuth2User customOAuth2User;

            @BeforeEach
            void setUp() {
                teamId = 1L;
                member = createMember();
                OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
                customOAuth2User = new CustomOAuth2User(oauth2UserDTO);

                SecurityUtil.setAuthentication(customOAuth2User);

                when(teamQueryService.existsByTeamId(teamId)).thenReturn(true);
            }

            @Test
            @DisplayName("200 Ok를 반환한다.")
            void it_returns_200_ok() throws Exception {
                mockMvc.perform(get("/api/v1/teams/{teamId}", teamId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
            }
        }
    }
}
