package com.yellobook.domain.member.controller;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.member.dto.response.ProfileResponse;
import com.yellobook.domain.member.service.MemberQueryService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private final TeamMemberVO teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    @BeforeEach
    void setTeamMemberVO() throws Exception{
        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(teamMemberVO);
    }

    @Test
    @DisplayName("사용자 프로필 조회")
    void getMemberProfile() throws Exception{
        //given
        ProfileResponse response = new ProfileResponse(1L, "yellow", "image", "email", Collections.emptyList());
        when(memberQueryService.getMemberProfile(teamMemberVO.getMemberId())).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/members/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname", CoreMatchers.is(response.nickname())))
                .andReturn();
    }
}