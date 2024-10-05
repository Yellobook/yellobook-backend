package com.yellobook.domains.inform.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inform.dto.request.CreateInformCommentRequest;
import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.inform.service.InformCommandService;
import com.yellobook.domains.inform.service.InformQueryService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Nested
    @DisplayName("createInform 메소드는")
    class Describe_createInform {

        @Nested
        @DisplayName("유효한 요청의 경우")
        class Context_Valid_Request {

            Long informId;
            CreateInformRequest request;
            CreateInformResponse response;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                informId = 1L;
                request = new CreateInformRequest("test1", "01012345678", List.of(), LocalDate.now());
                response = new CreateInformResponse(informId, LocalDateTime.now());

                teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);
                when(informCommandService.createInform(teamMemberVO.getMemberId(), request)).thenReturn(response);
            }

            @Test
            @DisplayName("201 Created를 반환한다.")
            void it_returns_201_created() throws Exception {
                mockMvc.perform(post("/api/v1/informs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("deleteInform 메소드는")
    class Describe_deleteInform {

        @Nested
        @DisplayName("inform이 존재하는 경우")
        class Context_Exist_Inform {

            Long informId;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                informId = 1L;

                teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(informRepository.existsById(informId)).thenReturn(true);
                doNothing().when(informCommandService)
                        .deleteInform(informId, teamMemberVO.getMemberId());
            }

            @Test
            @DisplayName("204 NoContent를 반환한다.")
            void it_returns_204_no_content() throws Exception {
                mockMvc.perform(delete("/api/v1/informs/{informId}", informId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }
        }
    }

    @Nested
    @DisplayName("getInform 메소드는")
    class Describe_getInform {

        @Nested
        @DisplayName("inform이 존재하는 경우")
        class Context_Exist_Inform {

            GetInformResponse response;
            Long informId;
            TeamMemberVO teamMemberVO;


            @BeforeEach
            void setUp() throws Exception {
                informId = 1L;
                response = new GetInformResponse("test", "test", "test", List.of(), 10, List.of(), LocalDate.now());

                teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(informRepository.existsById(informId)).thenReturn(true);
                when(informQueryService.getInformById(teamMemberVO.getMemberId(), informId)).thenReturn(response);
            }

            @Test
            @DisplayName("200 Ok, 그리고 해당 inform의 내용을 반환한다.")
            void it_returns_200_ok() throws Exception {
                mockMvc.perform(get("/api/v1/informs/{informId}", informId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.title", equalTo(response.title())))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("addComment 메소드는")
    class Describe_addComment {

        @Nested
        @DisplayName("유효한 요청이 들어왔을 경우")
        class Context_Valid_Request {

            CreateInformCommentRequest request;
            CreateInformCommentResponse response;
            Long informId;
            TeamMemberVO teamMemberVO;

            @BeforeEach
            void setUp() throws Exception {
                informId = 1L;

                request = new CreateInformCommentRequest("test");
                response = new CreateInformCommentResponse(informId, LocalDateTime.now());

                teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(informRepository.existsById(informId)).thenReturn(true);
                when(informCommandService.addComment(informId, teamMemberVO.getMemberId(), request)).thenReturn(
                        response);
            }

            @Test
            @DisplayName("201 Created를 반환한다.")
            void it_returns_201_created() throws Exception {
                mockMvc.perform(post("/api/v1/informs/{informId}/comment", informId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("increaseInformView 메소드는")
    class Describe_increaseInformView {

        @Nested
        @DisplayName("유효한 요청이 들어왔을 경우")
        class Context_Valid_Request {

            TeamMemberVO teamMemberVO;
            Long informId;

            @BeforeEach
            void setUp() throws Exception {
                teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
                informId = 1L;

                when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
                when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                        .thenReturn(teamMemberVO);

                when(informRepository.existsById(informId)).thenReturn(true);
                doNothing().when(informCommandService)
                        .increaseViewCount(informId, teamMemberVO);
            }

            @Test
            @DisplayName("204 noContent를 반환한다.")
            void it_returns_204_no_content() throws Exception {
                mockMvc.perform(patch("/api/v1/informs/{informId}/views", informId).
                                contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
