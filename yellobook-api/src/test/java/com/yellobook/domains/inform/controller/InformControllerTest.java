package com.yellobook.domains.inform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.inform.dto.request.CreateInformCommentRequest;
import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.inform.service.InformCommandService;
import com.yellobook.domains.inform.service.InformQueryService;
import com.yellobook.domains.member.entity.Member;
import fixture.MemberFixture;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InformController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class InformControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InformQueryService informQueryService;
    @MockBean
    private InformCommandService informCommandService;
    @MockBean
    private InformRepository informRepository;
    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    Member member;
    CustomOAuth2User customOAuth2User;

    private final Long informId = 1L;

    @BeforeEach
    void setUp() throws Exception {
        member = MemberFixture.createMember();

        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.from(member);
        customOAuth2User = new CustomOAuth2User(oAuth2UserDTO);

        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(customOAuth2User);
    }

    @Nested
    @DisplayName("ExistInform 애노테이션은")
    class Describe_ExistInform_Annotation{

        @Nested
        @DisplayName("Inform이 존재하는 경우")
        class Context_Exist_Inform{

            @BeforeEach
            void setUp() {
                when(informRepository.existsById(informId)).thenReturn(true);
            }

            @Test
            @DisplayName("200 OK를 반환한다.")
            void it_returns_200_ok()throws Exception{
                mockMvc.perform(get("/api/v1/informs/{informId}", informId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

            }
        }

        @Nested
        @DisplayName("Inform이 존재하지 않는 경우")
        class Context_Not_Exist_Inform{

            Long nonExistInformId;

            @BeforeEach
            void setUp() {
                nonExistInformId = 99L;

                when(informRepository.existsById(nonExistInformId)).thenReturn(false);
            }

            @Test
            @DisplayName("400 Bad Request를 반환한다.")
            void it_returns_400_bad_request()throws Exception{
                mockMvc.perform(get("/api/v1/informs/{informId}", nonExistInformId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
            }
        }
    }
}
