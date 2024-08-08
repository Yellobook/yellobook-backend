package com.yellobook.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domain.order.dto.request.MakeOrderRequest;
import com.yellobook.domain.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domain.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domain.order.dto.response.GetOrderResponse;
import com.yellobook.domain.order.dto.response.MakeOrderResponse;
import com.yellobook.domain.order.service.OrderCommandService;
import com.yellobook.domain.order.service.OrderQueryService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
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

    private final TeamMemberVO teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    @BeforeEach
    void setTeamMemberVO() throws Exception{
        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(teamMemberVO);
    }

    @Test
    @DisplayName("주문 작성")
    void makeOrder() throws Exception{
        //given
        MakeOrderRequest request = MakeOrderRequest.builder().productId(1L).build();
        MakeOrderResponse response = MakeOrderResponse.builder().orderId(2L).build();
        when(orderCommandService.makeOrder(request, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/orders")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId", CoreMatchers.is(response.orderId().intValue())))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("주문 조회 - 주문이 존재 할때")
    void getOrder() throws Exception{
        //given
        Long orderId = 1L;
        GetOrderResponse response = GetOrderResponse.builder().writer("writer").build();
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
        when(orderQueryService.getOrder(orderId, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.writer", CoreMatchers.is(response.writer())))
                .andReturn();
    }

    @Test
    @DisplayName("@ExistOrder - 주문이 존재 안하면 예외 발생")
    void validExistOrder() throws Exception{
        //given
        Long orderId = 1L;
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(false);

        //when & then
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("주문에 댓글 달기 - 주문이 존재 할 때")
    void addOrderComment() throws Exception{
        //given
        Long orderId = 1L;
        AddOrderCommentRequest request = AddOrderCommentRequest.builder().content("content").build();
        AddOrderCommentResponse response = AddOrderCommentResponse.builder().commentId(1L).build();
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
        when(orderCommandService.addOrderComment(orderId, teamMemberVO, request)).thenReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/orders/{orderId}/comment", orderId)
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentId", CoreMatchers.is(response.commentId().intValue())))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("주문 댓글 조회 - 주문이 존재 할 때")
    void getOrderComments() throws Exception{
        //given
        Long orderId = 1L;
        GetOrderCommentsResponse response = GetOrderCommentsResponse.builder().comments(Collections.emptyList()).build();
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
        when(orderQueryService.getOrderComments(orderId, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/orders/{orderId}/comment", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.comments", CoreMatchers.is(response.comments())))
                .andReturn();
    }

    @Test
    @DisplayName("주문 정정 요청 - 주문이 존재 할 때")
    void modifyRequestOrder() throws Exception{
        //given
        Long orderId = 1L;
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
        doNothing().when(orderCommandService).modifyRequestOrder(orderId, teamMemberVO);

        //when & then
        mockMvc.perform(patch("/api/v1/orders/{orderId}/correction", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("주문 확정 - 주문이 존재 할 때")
    void confirmOrder() throws Exception{
        //given
        Long orderId = 1L;
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
        doNothing().when(orderCommandService).confirmOrder(orderId, teamMemberVO);

        //when & then
        mockMvc.perform(patch("/api/v1/orders/{orderId}/confirm", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("주문 취소 - 주문이 존재할 때")
    void cancelOrder() throws Exception{
        //given
        Long orderId = 1L;
        when(orderQueryService.existsByOrderId(orderId)).thenReturn(true);
        doNothing().when(orderCommandService).cancelOrder(orderId, teamMemberVO);

        //when & then
        mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

}