package com.yellobook.domain.order.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.order.dto.AddOrderCommentRequest;
import com.yellobook.domain.order.dto.AddOrderCommentResponse;
import com.yellobook.domain.order.dto.MakeOrderRequest;
import com.yellobook.domain.order.mapper.OrderMapper;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.repository.OrderCommentRepository;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCommandServiceTest {
    @InjectMocks
    private OrderCommandService orderCommandService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMentionRepository orderMentionRepository;
    @Mock
    private OrderCommentRepository orderCommentRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderMapper orderMapper;

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
            Order order = createOrderAmount(OrderStatus.PENDING_CONFIRM);
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
            Order order = createOrderAmount(OrderStatus.CONFIRMED);
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
            Order order = createOrderAmount(OrderStatus.PENDING_CONFIRM);
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
            Order order = createOrderAmount(OrderStatus.PENDING_CONFIRM);
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
            Order order = createOrderAmount(OrderStatus.PENDING_MODIFY);
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
            Order order = createOrderAmount(10, 100);
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
            Order order = createOrderAmount(100, 10);
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

    @Nested
    @DisplayName("주문 취소")
    class CancelOrderTests{
        @Test
        @DisplayName("내가 작성한 주문이 아니면 취소 불가능")
        void cantCancelNotMineOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_MODIFY, 2L);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.cancelOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
        }

        @Test
        @DisplayName("주문 정정 상태가 아니면 취소 불가능")
        void cantCancelPendingModifyOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.CONFIRMED, 1L);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.cancelOrder(orderId, admin));
            Assertions.assertEquals(OrderErrorCode.ORDER_CANT_CANCEL, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
        }

        @Test
        @DisplayName("주문이 잘 취소 되었는지 확인")
        void cancelOrder(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_MODIFY, 1L);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            //when
            orderCommandService.cancelOrder(orderId, admin);

            //then
            verify(orderRepository).findById(orderId);
            verify(orderMentionRepository).deleteAllByOrderId(order.getId());
            verify(orderRepository).delete(order);
        }

    }

    @Nested
    @DisplayName("주문 댓글 추가")
    class AddOrderComment{
        @Test
        @DisplayName("해당 주문의 주문자가 아니면 댓글 작성 불가능")
        void nonOrdererCantAddOrderComment(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 1L);  // orderMember : 1L
            Member member = createMember(orderer.getMemberId());  // member : 2L
            AddOrderCommentRequest request = createAddOrderCommentRequest();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
            when(orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())).thenReturn(false);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.addOrderComment(orderId, orderer, request));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(memberRepository).findById(orderer.getMemberId());
        }

        @Test
        @DisplayName("해당 주문에 언급된 관리자가 아니면 댓글 작성 불가능")
        void nonAdminCantAddOrderComment(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 2L);  // orderMember : 2L
            Member member = createMember(admin.getMemberId());  // member : 1L
            AddOrderCommentRequest request = createAddOrderCommentRequest();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
            when(orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())).thenReturn(false);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.addOrderComment(orderId, admin, request));
            Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
            verify(orderRepository).findById(orderId);
            verify(memberRepository).findById(admin.getMemberId());
        }

        @Test
        @DisplayName("주문자가 맞을 때 주문 댓글 추가가 잘 되는지 확인")
        void addOrderComment(){
            //given
            Long orderId = 1L;
            Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 1L);  // orderMember : 1L
            Member member = createMember(admin.getMemberId());  // member : 1L
            AddOrderCommentRequest request = createAddOrderCommentRequest();
            OrderComment comment = createOrderCommentWithId(1L);
            Long commentId = 1L;
            AddOrderCommentResponse expectResponse = AddOrderCommentResponse.builder().commentId(1L).build();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
            when(orderMapper.toOrderComment(request, member, order)).thenReturn(comment);
            when(orderCommentRepository.save(any(OrderComment.class))).thenReturn(comment);
            when(orderMapper.toAddOrderCommentResponse(commentId)).thenReturn(expectResponse);

            //when
            AddOrderCommentResponse response = orderCommandService.addOrderComment(orderId, admin, request);

            //then
            assertThat(response).isNotNull();
            assertThat(response.getCommentId()).isEqualTo(expectResponse.getCommentId());
            verify(orderRepository).findById(orderId);
            verify(memberRepository).findById(admin.getMemberId());
            verify(orderCommentRepository).save(any(OrderComment.class));
            verify(orderMapper).toAddOrderCommentResponse(commentId);
        }

        private AddOrderCommentRequest createAddOrderCommentRequest(){
            return AddOrderCommentRequest.builder()
                    .content("댓글 내용")
                    .build();
        }
    }

    @Nested
    @DisplayName("주문 생성")
    class MakeOrderTests{
        @Test
        @DisplayName("관리자는 주문 생성 불가능")
        void adminCantMakeOrder(){
            //given
            MakeOrderRequest request = createMakeOrderRequest(10);
            Member member = createMember(admin.getMemberId());
            Team team = createTeam(admin.getTeamId());
            when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
            when(teamRepository.findById(admin.getTeamId())).thenReturn(Optional.of(team));

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.makeOrder(request, admin));
            Assertions.assertEquals(AuthErrorCode.ADMIN_NOT_ALLOWED, exception.getErrorCode());
            verify(memberRepository).findById(admin.getMemberId());
            verify(teamRepository).findById(admin.getTeamId());
        }

        @Test
        @DisplayName("뷰어는 주문 생성 불가능")
        void viewerCantMakeOrder(){
            //given
            MakeOrderRequest request = createMakeOrderRequest(10);
            Member member = createMember(viewer.getMemberId());
            Team team = createTeam(viewer.getTeamId());
            when(memberRepository.findById(viewer.getMemberId())).thenReturn(Optional.of(member));
            when(teamRepository.findById(viewer.getTeamId())).thenReturn(Optional.of(team));

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.makeOrder(request, viewer));
            Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            verify(memberRepository).findById(viewer.getMemberId());
            verify(teamRepository).findById(viewer.getTeamId());
        }

        @Test
        @DisplayName("관리자가 없는 팀 스페이스에서 주문 생성 불가능")
        void notExistAdminCantMakeOrder(){
            //given
            MakeOrderRequest request = createMakeOrderRequest(10);
            Member member = createMember(orderer.getMemberId());
            Team team = createTeam(orderer.getTeamId());
            when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
            when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
            when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.empty());

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.makeOrder(request, orderer));
            Assertions.assertEquals(OrderErrorCode.ORDER_CREATION_NOT_ALLOWED, exception.getErrorCode());
            verify(memberRepository).findById(orderer.getMemberId());
            verify(teamRepository).findById(orderer.getTeamId());
            verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
        }

        @Test
        @DisplayName("존재하지 않는 제품에 주문 불가능")
        void notExistProductCantMakeOrder(){
            //given
            MakeOrderRequest request = createMakeOrderRequest(10);
            Member member = createMember(orderer.getMemberId());
            Team team = createTeam(orderer.getTeamId());
            Participant participant = createParticipant();
            when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
            when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
            when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.of(participant));
            when(productRepository.findById(request.getProductId())).thenReturn(Optional.empty());

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.makeOrder(request, orderer));
            Assertions.assertEquals(InventoryErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
            verify(memberRepository).findById(orderer.getMemberId());
            verify(teamRepository).findById(orderer.getTeamId());
            verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
            verify(productRepository).findById(request.getProductId());
        }

        @Test
        @DisplayName("제품 수량보다 많은 수량 주문 불가능")
        void exceedProductAmountCantMakeOrder(){
            //given
            MakeOrderRequest request = createMakeOrderRequest(1111);
            Member member = createMember(orderer.getMemberId());
            Team team = createTeam(orderer.getTeamId());
            Participant participant = createParticipant();
            Product product = Product.builder().amount(10).build();
            when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
            when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
            when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.of(participant));
            when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    orderCommandService.makeOrder(request, orderer));
            Assertions.assertEquals(OrderErrorCode.ORDER_AMOUNT_EXCEED, exception.getErrorCode());
            verify(memberRepository).findById(orderer.getMemberId());
            verify(teamRepository).findById(orderer.getTeamId());
            verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
            verify(productRepository).findById(request.getProductId());
        }

        private MakeOrderRequest createMakeOrderRequest(Integer orderAmount){
            return MakeOrderRequest.builder().productId(1L).orderAmount(orderAmount).build();
        }

    }

    private Order createOrderAmount(OrderStatus status){
        return Order.builder().orderStatus(status).build();
    }

    private Order createOrderAmount(Integer productAmount, Integer orderAmount){
        Product product = Product.builder().amount(productAmount).build();
        return Order.builder().orderStatus(OrderStatus.PENDING_CONFIRM).product(product).orderAmount(orderAmount).build();
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

    private Team createTeam(Long teamId){
        Team team = Team.builder().build();
        try {
            Field idField = Team.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(team, teamId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set member ID", e);
        }
        return team;
    }

    private Participant createParticipant(){
        Member member = Member.builder().build();
        return Participant.builder().member(member).build();
    }

    private OrderComment createOrderCommentWithId(Long commentId){
        OrderComment comment = OrderComment.builder().build();
        try {
            Field idField = OrderComment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(comment, commentId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set order comment ID", e);
        }
        return comment;
    }

}
