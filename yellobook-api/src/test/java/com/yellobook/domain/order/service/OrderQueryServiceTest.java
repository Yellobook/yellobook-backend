package com.yellobook.domain.order.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.order.dto.GetOrderCommentsResponse;
import com.yellobook.domain.order.dto.GetOrderCommentsResponse.CommentInfo;
import com.yellobook.domain.order.mapper.OrderMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.OrderCommentDTO;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    private final TeamMemberVO admin = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO viewer = TeamMemberVO.of(3L, 1L, MemberTeamRole.VIEWER);

    @Nested
    @DisplayName("주문 댓글 조회")
    class GetOrderComments{
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
            CommentInfo commentInfo = CommentInfo.builder().content("content").role(MemberTeamRole.ORDERER).build();
            List<CommentInfo> commentInfos = List.of(commentInfo);

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.getOrderComments(orderId)).thenReturn(List.of(OrderCommentDTO.builder().build()));
            when(orderMapper.toCommentInfo(any(OrderCommentDTO.class))).thenReturn(commentInfo);

            //when
            GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, orderer);

            //then
            assertThat(response).isNotNull();
            assertThat(response.getComments().size()).isEqualTo(1);
        }

    }

//    @Nested
//    @DisplayName("주문 단건 조회")


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