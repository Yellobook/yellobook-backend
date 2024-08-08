package com.yellobook.domain.order.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.entity.Product;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCommandServiceTest {
    @InjectMocks
    private OrderCommandService orderCommandService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMentionRepository orderMentionRepository;

    private final TeamMemberVO admin = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO viewer = TeamMemberVO.of(3L, 1L, MemberTeamRole.VIEWER);

    @Nested
    @DisplayName("주문 정정 요청")
    class ModifyRequestOrderTests{
        @Test
        @DisplayName("함께하는 사람에 내가 없으면 (관리자가 아니라는 뜻, 접근권한 에러)")
        void cantModifyRequestOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(OrderStatus.PENDING_CONFIRM);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(false);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.modifyRequestOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }

        @Test
        @DisplayName("주문 확인 상태이면 주문 정정 요청 불가능")
        void CantModifyConfirmedOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(OrderStatus.CONFIRMED);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.modifyRequestOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_CONFIRMED_CANT_MODIFY, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }

        @Test
        @DisplayName("주문 정정 요청이 잘 일어나는지 확인")
        void modifyRequestOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(OrderStatus.PENDING_CONFIRM);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);

            //when
            orderCommandService.modifyRequestOrder(orderId, admin);

            //then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING_MODIFY);
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }
    }

    @Nested
    @DisplayName("주문 확정")
    class ConfirmOrderTest{
        @Test
        @DisplayName("함께하는 사람에 내가 없으면 (관리자가 아니라는 뜻, 접근권한 에러)")
        void cantConfirmOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(OrderStatus.PENDING_CONFIRM);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(false);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.confirmOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }

        @Test
        @DisplayName("주문 정정 요청이 되어 있으면 주문 확정 불가능")
        void cantConfirmPendingModifyOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(OrderStatus.PENDING_MODIFY);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.confirmOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_PENDING_MODIFY_CANT_CONFIRM, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }

        @Test
        @DisplayName("현재 재고 수량 보다 많은 수량의 주문 확정 불가능")
        void cantConfirmExceedAmountOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(10, 100);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.confirmOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_AMOUNT_EXCEED, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }

        @Test
        @DisplayName("주문 확정이 잘 일어났는지 확인")
        void confirmOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrder(100, 10);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);

            //when
            orderCommandService.confirmOrder(orderId, admin);

            //then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CONFIRMED);
            assertThat(order.getProduct().getAmount()).isEqualTo(100-10);
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
        }

    }


    private Order createOrder(OrderStatus status){
        return Order.builder()
                .orderStatus(status)
                .build();
    }

    private Order createOrder(Integer productAmount, Integer orderAmount){
        Product product = Product.builder().amount(productAmount).build();
        return Order.builder().orderStatus(OrderStatus.PENDING_CONFIRM).product(product).orderAmount(orderAmount).build();
    }

}
