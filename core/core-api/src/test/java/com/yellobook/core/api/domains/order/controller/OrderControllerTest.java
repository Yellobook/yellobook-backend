package com.yellobook.core.api.domains.order.controller;

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
import com.yellobook.core.api.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.core.api.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.core.api.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.core.api.domains.order.dto.response.AddOrderCommentResponse;
import com.yellobook.core.api.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.core.api.domains.order.dto.response.GetOrderResponse;
import com.yellobook.core.api.domains.order.dto.response.MakeOrderResponse;
import com.yellobook.core.api.domains.order.service.OrderCommandService;
import com.yellobook.core.api.domains.order.service.OrderQueryService;
import com.yellobook.core.common.vo.TeamMemberVO;
import com.yellobook.core.core.enums.TeamMemberRole;
import java.time.LocalDate;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderController Unit Test")
class OrderControllerTest {
    private final TeamMemberVO teamMemberVO = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderQueryService orderQueryService;
    @MockBean
    private OrderCommandService orderCommandService;
    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    @BeforeEach
    void setTeamMemberVO() throws Exception {
        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(teamMemberVO);
    }

    @Nested
    @DisplayName("makeOrder 메소드는")
    class Describe_MakeOrder {
        @Nested
        @DisplayName("주문을 생성하면")
        class Context_order_given {
            MakeOrderRequest request;
            MakeOrderResponse response;

            @BeforeEach
            void setUpContext() {
                request = MakeOrderRequest.builder()
                        .productId(1L)
                        .orderAmount(1)
                        .date(LocalDate.now())
                        .build();
                response = MakeOrderResponse.builder()
                        .orderId(2L)
                        .build();
                when(orderCommandService.makeOrder(request, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("새로운 주문을 추가한다.")
            void it_adds_new_order() throws Exception {
                mockMvc.perform(post("/api/v1/orders")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType("application/json;charset=UTF-8"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.orderId", CoreMatchers.is(response.orderId()
                                .intValue())))
                        .andDo(print())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("getOrder 메소드는")
    class Describe_GetOrder {
        @Nested
        @DisplayName("유효한 주문 Id이면")
        class Context_order_id_exist {
            Long orderId;
            GetOrderResponse response;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                response = GetOrderResponse.builder()
                        .writer("writer")
                        .build();
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
                when(orderQueryService.getOrder(orderId, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("해당 주문을 반환한다.")
            void it_returns_order() throws Exception {
                mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.writer", CoreMatchers.is(response.writer())))
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("유효한 주문 Id가 아니면")
        class Context_order_id_not_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(false);
            }

            @Test
            @DisplayName("상태 코드 400을 반환한다.")
            void it_returns_400() throws Exception {
                mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("addOrderComment 메서드는")
    class Describe_AddOrderComment {
        @Nested
        @DisplayName("유효한 주문 Id이면")
        class Context_product_id_exist {
            Long orderId;
            AddOrderCommentRequest request;
            AddOrderCommentResponse response;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                request = AddOrderCommentRequest.builder()
                        .content("content")
                        .build();
                response = AddOrderCommentResponse.builder()
                        .commentId(1L)
                        .build();
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
                when(orderCommandService.addOrderComment(orderId, teamMemberVO, request)).thenReturn(response);
            }

            @Test
            @DisplayName("해당 주문에 댓글을 추가한다.")
            void it_adds_new_order_comment() throws Exception {
                mockMvc.perform(post("/api/v1/orders/{orderId}/comment", orderId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType("application/json;charset=UTF-8"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.commentId", CoreMatchers.is(response.commentId()
                                .intValue())))
                        .andDo(print())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("getOrderComments 메소드는")
    class Describe_GetOrderComments {
        @Nested
        @DisplayName("유효한 주문 Id이면")
        class Context_product_id_exist {
            Long orderId;
            GetOrderCommentsResponse response;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                response = GetOrderCommentsResponse.builder()
                        .comments(Collections.emptyList())
                        .build();
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
                when(orderQueryService.getOrderComments(orderId, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("주문 댓글 조회를 한다.")
            void it_returns_order_comments() throws Exception {
                mockMvc.perform(get("/api/v1/orders/{orderId}/comment", orderId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.comments", CoreMatchers.is(response.comments())))
                        .andReturn();
            }
        }
    }


    @Nested
    @DisplayName("modifyRequestOrder 메소드는")
    class Describe_ModifyRequestOrder {
        @Nested
        @DisplayName("유효한 주문 Id이면")
        class Context_product_id_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
                doNothing().when(orderCommandService)
                        .modifyRequestOrder(orderId, teamMemberVO);
            }

            @Test
            @DisplayName("주문 정정 요청을 수행한다.")
            void it_modify_request_order() throws Exception {
                mockMvc.perform(patch("/api/v1/orders/{orderId}/correction", orderId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
            }
        }
    }


    @Nested
    @DisplayName("confirmOrder 메소드는")
    class Describe_ConfirmOrder {
        @Nested
        @DisplayName("유효한 주문 Id이면")
        class Context_product_id_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
                doNothing().when(orderCommandService)
                        .confirmOrder(orderId, teamMemberVO);
            }

            @Test
            @DisplayName("주문을 확정한다.")
            void it_confirms_order() throws Exception {
                mockMvc.perform(patch("/api/v1/orders/{orderId}/confirm", orderId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("cancelOrder 메소드는")
    class Describe_CancelOrder {
        @Nested
        @DisplayName("유효한 주문 Id이면")
        class Context_product_id_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
                doNothing().when(orderCommandService)
                        .cancelOrder(orderId, teamMemberVO);
            }

            @Test
            @DisplayName("주문을 취소한다.")
            void it_cancel_order() throws Exception {
                mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
            }
        }
    }
}
