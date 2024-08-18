package com.yellobook.domains.member.controller;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.service.MemberQueryService;
import org.apache.catalina.User;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberQueryService memberQueryService;
    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    private Authentication authentication;

    @BeforeEach
    void setTeamMemberVO() throws Exception{
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
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("사용자 프로필 조회")
    void getMemberProfile() throws Exception{
        //given
        ProfileResponse response = new ProfileResponse(1L, "yellow", "image", "email", Collections.emptyList());
        when(memberQueryService.getMemberProfile(any(Long.class))).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/members/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(authentication)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname", CoreMatchers.is(response.nickname())))
                .andReturn();
    }
}
