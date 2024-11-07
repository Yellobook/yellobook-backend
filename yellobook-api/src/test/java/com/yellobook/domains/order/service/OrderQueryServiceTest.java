package com.yellobook.domains.order.service;

import static com.yellobook.common.enums.OrderStatus.PENDING_CONFIRM;
import static fixture.MemberFixture.createMember;
import static fixture.OrderFixture.createOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static support.ReflectionUtil.setField;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse.CommentInfo;
import com.yellobook.domains.order.dto.response.GetOrderResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.mapper.OrderMapper;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.error.code.OrderErrorCode;
import com.yellobook.error.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderQueryService Unit Test")
class OrderQueryServiceTest {
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, TeamMemberRole.ORDERER);
    @InjectMocks
    private OrderQueryService orderQueryService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMentionRepository orderMentionRepository;
    @Mock
    private OrderMapper orderMapper;

    private Member createMemberWithId(Long memberId) {
        Member member = createMember();
        setField(member, "id", memberId);
        return member;
    }

    @Nested
    @DisplayName("getOrderComments 메소드는")
    class Describe_getOrderComments {
        @Nested
        @DisplayName("주문이 존재하지 않으면")
        class Context_order_not_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderQueryService.getOrderComments(orderId, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("해당 주문의 주문자, 관리자가 아니면")
        class Context_not_order_orderer_or_admin {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(1L);
                Order order = createOrder(null, member, null, PENDING_CONFIRM);  // orderMember : 1L, member : 2L
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(orderer.getMemberId(), orderId)).thenReturn(
                        false);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderQueryService.getOrderComments(orderId, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("댓글이 없으면")
        class Context_empty_orderComment {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(2L);
                Order order = createOrder(null, member, null, PENDING_CONFIRM);  // orderMember : 2L, member : 2L
                List<QueryOrderComment> queryOrderComments = Collections.emptyList();
                GetOrderCommentsResponse expectResponse = GetOrderCommentsResponse.builder()
                        .orderId(orderId)
                        .comments(Collections.emptyList())
                        .build();

                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderRepository.getOrderComments(orderId)).thenReturn(queryOrderComments);
                when(orderMapper.toGetOrderCommentsResponse(orderId, queryOrderComments)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void getEmptyOrderComments() {
                GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, orderer);

                assertThat(response).isNotNull();
                assertThat(response.comments()
                        .size()).isEqualTo(0);
                assertThat(response.comments()).isEmpty();
            }
        }

        @Nested
        @DisplayName("댓글이 존재하면")
        class Context_orderComment_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(2L);
                Order order = createOrder(null, member, null, PENDING_CONFIRM);  // orderMember : 2L, member : 2L
                CommentInfo commentInfo = CommentInfo.builder()
                        .content("content")
                        .role("주문자")
                        .build();
                List<QueryOrderComment> queryOrderComments = List.of(QueryOrderComment.builder()
                        .commentId(1L)
                        .role(TeamMemberRole.ORDERER)
                        .content("content")
                        .createdAt(LocalDateTime.now())
                        .build());
                GetOrderCommentsResponse expectResponse = GetOrderCommentsResponse.builder()
                        .orderId(orderId)
                        .comments(Collections.singletonList(commentInfo))
                        .build();

                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderRepository.getOrderComments(orderId)).thenReturn(queryOrderComments);
                when(orderMapper.toGetOrderCommentsResponse(orderId, queryOrderComments)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("주문의 댓글을 반환한다.")
            void getOrderComments() {
                GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, orderer);

                assertThat(response).isNotNull();
                assertThat(response.comments()
                        .size()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("getOrder 메소드는")
    class Describe_GetOrder {
        @Nested
        @DisplayName("주문이 존재하지 않으면")
        class Context_order_not_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderQueryService.getOrder(orderId, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("해당 주문의 주문자가 아니거나 관리자가 아니면")
        class Context_not_order_orderer_or_admin {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(1L);
                Order order = createOrder(null, member, null, PENDING_CONFIRM);  // orderMember : 1L, member : 2L
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(orderer.getMemberId(), orderId)).thenReturn(
                        false);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderQueryService.getOrder(orderId, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("주문이 존재하면")
        class Context_order_exist {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(2L);
                Order order = createOrder(null, member, null, PENDING_CONFIRM);  // orderMember : 2L, member : 2L
                QueryOrder queryOrder = QueryOrder.builder()
                        .build();
                GetOrderResponse expectResponse = GetOrderResponse.builder()
                        .build();

                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderRepository.getOrder(orderId)).thenReturn(queryOrder);
                when(orderMapper.toGetOrderResponse(queryOrder)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("주문 정보를 반환한다.")
            void getOrder() {
                GetOrderResponse response = orderQueryService.getOrder(orderId, orderer);

                assertThat(response).isNotNull();
            }
        }
    }

}
