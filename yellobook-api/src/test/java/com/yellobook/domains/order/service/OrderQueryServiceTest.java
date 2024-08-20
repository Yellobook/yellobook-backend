package com.yellobook.domains.order.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse.CommentInfo;
import com.yellobook.domains.order.dto.response.GetOrderResponse;
import com.yellobook.domains.order.mapper.OrderMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.error.code.OrderErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {
    @InjectMocks
    private OrderQueryService orderQueryService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMentionRepository orderMentionRepository;
    @Mock
    private OrderMapper orderMapper;

    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);

    @Nested
    @DisplayName("주문 댓글 조회")
    class GetOrderComments{

        @Test
        @DisplayName("주문이 존재하지 않으면 주문 불가능")
        void notValidOrder(){
            //given
            Long orderId = 1L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderQueryService.getOrderComments(orderId, orderer));
            Assertions.assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("해당 주문의 주문자가 아니거나 관리자가 아니면 댓글 조회 불가능")
        void cantAccessOrderComment(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 1L);  // orderMember : 1L, member : 2L
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(orderer.getMemberId(), orderId)).thenReturn(false);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderQueryService.getOrderComments(orderId, orderer));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
        }

        @Test
        @DisplayName("댓글 조회가 잘 되는지 확인")
        void getOrderComments(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 2L);  // orderMember : 2L, member : 2L
            CommentInfo commentInfo = CommentInfo.builder().content("content").role("주문자").build();

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.getOrderComments(orderId)).thenReturn(List.of(QueryOrderComment.builder().build()));
            when(orderMapper.toCommentInfo(any(QueryOrderComment.class))).thenReturn(commentInfo);

            //when
            GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, orderer);

            //then
            assertThat(response).isNotNull();
            assertThat(response.comments().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("주문 댓글이 없으면 빈 리스트 반환")
        void getEmptyOrderComments(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 2L);  // orderMember : 2L, member : 2L

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.getOrderComments(orderId)).thenReturn(Collections.emptyList());

            //when
            GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, orderer);

            //then
            assertThat(response).isNotNull();
            assertThat(response.comments().size()).isEqualTo(0);
            assertThat(response.comments()).isEmpty();
        }


    }

    @Nested
    @DisplayName("주문 단건 조회")
    class GetOrderTests{
        @Test
        @DisplayName("주문이 존재하지 않으면 주문 불가능")
        void notValidOrder(){
            //given
            Long orderId = 1L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderQueryService.getOrder(orderId, orderer));
            Assertions.assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("해당 주문의 주문자가 아니거나 관리자가 아니면 댓글 조회 불가능")
        void cantAccessOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 1L);  // orderMember : 1L, member : 2L
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(orderer.getMemberId(), orderId)).thenReturn(false);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderQueryService.getOrder(orderId, orderer));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
        }

        @Test
        @DisplayName("주문 조회가 잘 되는지 확인")
        void getOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 2L);  // orderMember : 2L, member : 2L
            QueryOrder queryOrder = QueryOrder.builder().build();
            GetOrderResponse expectResponse = GetOrderResponse.builder().build();

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.getOrder(orderId)).thenReturn(queryOrder);
            when(orderMapper.toGetOrderResponse(queryOrder)).thenReturn(expectResponse);

            //when
            GetOrderResponse response = orderQueryService.getOrder(orderId, orderer);

            //then
            assertThat(response).isNotNull();
        }
    }


    private Order createOrderMember(OrderStatus status, Long memberId){
        Member member = createMember(memberId);
        return Order.builder().member(member).orderStatus(status).build();
    }

    private Member createMember(Long memberId){
        Member member = Member.builder().build();
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, memberId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set member ID", e);
        }
        return member;
    }


}