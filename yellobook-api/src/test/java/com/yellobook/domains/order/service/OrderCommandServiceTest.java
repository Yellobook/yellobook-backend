package com.yellobook.domains.order.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.mapper.OrderMapper;
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
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.NestedTestConfiguration;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderCommandService Unit Test")
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
    @DisplayName("modifyRequestOrder 메소드는")
    class Describe_ModifyRequestOrder{
        @Nested
        @DisplayName("해당 주문에 언급된 관리자가 아니면")
        class Context_Not_Order_Admin{
            Long orderId;
            Order order;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(OrderStatus.PENDING_CONFIRM);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(false);
            }
            @Test
            @DisplayName("주문에 접근하지 못하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.modifyRequestOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("주문 확인 상태이면")
        class Context_Order_Confirmed{
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(OrderStatus.CONFIRMED);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);
            }
            @Test
            @DisplayName("주문 정정 요청이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.modifyRequestOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_CONFIRMED_CANT_MODIFY, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("주문 정정 요청이 가능하면")
        class Context_Can_Request_Modify_Order{
            Long orderId;
            Order order;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(OrderStatus.PENDING_CONFIRM);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);
            }
            @Test
            @DisplayName("주문 정정 요청을 수행한다.")
            void it_request_modify_order(){
                orderCommandService.modifyRequestOrder(orderId, admin);

                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING_MODIFY);
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

    }

    @Nested
    @DisplayName("confirmOrder 메소드는")
    class Describe_ConfirmOrder{
        @Nested
        @DisplayName("해당 주문에 언급된 관리자가 아니면")
        class Context_Not_Order_Admin{
            Long orderId;
            Order order;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(OrderStatus.PENDING_CONFIRM);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(false);
            }

            @Test
            @DisplayName("해당 주문에 접근할 수 없기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.confirmOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("주문 정정 요청 상태이면")
        class Context_Order_Pending_Modify{
            Long orderId;
            Order order;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(OrderStatus.PENDING_MODIFY);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);
            }
            @Test
            @DisplayName("주문 확정이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.confirmOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_PENDING_MODIFY_CANT_CONFIRM, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }
        @Nested
        @DisplayName("현재 재고 수량보다 많은 수량의 주문이면")
        class Context_Exceed_Amount{
            Long orderId;
            Order order;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(10, 100);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);
            }

            @Test
            @DisplayName("주문 확정이 불가능하므로 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.confirmOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_AMOUNT_EXCEED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }


        @Nested
        @DisplayName("주문 확정이 가능하면")
        class Context_Can_Confirm_Order{
            Long orderId;
            Order order;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderAmount(100, 10);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(true);
            }

            @Test
            @DisplayName("주문 확정을 수행한다.")
            void it_confirms_order(){
                orderCommandService.confirmOrder(orderId, admin);

                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CONFIRMED);
                assertThat(order.getProduct().getAmount()).isEqualTo(100-10);
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }
    }

    @Nested
    @DisplayName("cancelOrder 메소드는")
    class Describe_CancelOrder{
        @Nested
        @DisplayName("내가 작성한 주문이 아니면")
        class Context_Not_My_Order{
            Long orderId;

            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                Order order = createOrderMember(OrderStatus.PENDING_MODIFY, 2L);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("취소가 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.cancelOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
            }
        }

        @Nested
        @DisplayName("주문 정정 상태가 아니면")
        class Context_Order_Not_Pending_Modify{
            Long orderId;
            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                Order order = createOrderMember(OrderStatus.CONFIRMED, 1L);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("취소가 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.cancelOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_CANT_CANCEL, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
            }
        }

        @Nested
        @DisplayName("주문 취소가 가능하면")
        class Context_Can_Cancel_Order{
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                order = createOrderMember(OrderStatus.PENDING_MODIFY, 1L);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("주문을 취소한다.")
            void it_cancel_order(){
                orderCommandService.cancelOrder(orderId, admin);

                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).deleteAllByOrderId(order.getId());
                verify(orderRepository).delete(order);
            }
        }
    }

    @Nested
    @DisplayName("addOrderComment 메소드는")
    class Describe_AddOrderComment{
        @Nested
        @DisplayName("해당 주문의 주문자가 아니면")
        class Context_Not_Order_Orderer{
            Long orderId;
            AddOrderCommentRequest request;

            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 1L);  // orderMember : 1L
                Member member = createMember(orderer.getMemberId());  // member : 2L
                request = createAddOrderCommentRequest();
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())).thenReturn(false);
            }

            @Test
            @DisplayName("댓글 작성이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.addOrderComment(orderId, orderer, request));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(memberRepository).findById(orderer.getMemberId());
            }
        }

        @Nested
        @DisplayName("해당 주문에 언급된 관리자가 아니면")
        class Context_Not_Order_Admin{
            Long orderId;
            AddOrderCommentRequest request;

            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 2L);  // orderMember : 2L
                Member member = createMember(admin.getMemberId());  // member : 1L
                request = createAddOrderCommentRequest();
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
                when(orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())).thenReturn(false);
            }
            @Test
            @DisplayName("댓글 작성이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.addOrderComment(orderId, admin, request));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(memberRepository).findById(admin.getMemberId());
            }
        }

        @Nested
        @DisplayName("해당 주문의 주문자가 맞으면")
        class Context_Order_Orderer{
            Long orderId;
            Long commentId;
            AddOrderCommentRequest request;
            AddOrderCommentResponse expectResponse;

            @BeforeEach
            void setUpContext(){
                orderId = 1L;
                Order order = createOrderMember(OrderStatus.PENDING_CONFIRM, 1L);  // orderMember : 1L
                Member member = createMember(admin.getMemberId());  // member : 1L
                request = createAddOrderCommentRequest();
                OrderComment comment = createOrderCommentWithId(1L);
                commentId = 1L;
                expectResponse = AddOrderCommentResponse.builder().commentId(1L).build();
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
                when(orderMapper.toOrderComment(request, member, order)).thenReturn(comment);
                when(orderCommentRepository.save(any(OrderComment.class))).thenReturn(comment);
                when(orderMapper.toAddOrderCommentResponse(commentId)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("주문에 댓글을 추가한다.")
            void it_adds_order_comment(){
                AddOrderCommentResponse response = orderCommandService.addOrderComment(orderId, admin, request);

                assertThat(response).isNotNull();
                assertThat(response.commentId()).isEqualTo(expectResponse.commentId());
                verify(orderRepository).findById(orderId);
                verify(memberRepository).findById(admin.getMemberId());
                verify(orderCommentRepository).save(any(OrderComment.class));
                verify(orderMapper).toAddOrderCommentResponse(commentId);
            }
        }

        private AddOrderCommentRequest createAddOrderCommentRequest(){
            return AddOrderCommentRequest.builder()
                    .content("댓글 내용")
                    .build();
        }
    }

    @Nested
    @DisplayName("makeOrder 메소드는")
    class Describe_MakeOrder{
        @Nested
        @DisplayName("관리자라면")
        class Context_Admin{
            MakeOrderRequest request;
            @BeforeEach
            void setUpContext(){
                request = createMakeOrderRequest(10);
                Member member = createMember(admin.getMemberId());
                Team team = createTeam(admin.getTeamId());
                when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(admin.getTeamId())).thenReturn(Optional.of(team));
            }
            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, admin));
                Assertions.assertEquals(AuthErrorCode.ADMIN_NOT_ALLOWED, exception.getErrorCode());
                verify(memberRepository).findById(admin.getMemberId());
                verify(teamRepository).findById(admin.getTeamId());
            }
        }

        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer{
            MakeOrderRequest request;
            @BeforeEach
            void setUpContext(){
                request = createMakeOrderRequest(10);
                Member member = createMember(viewer.getMemberId());
                Team team = createTeam(viewer.getTeamId());
                when(memberRepository.findById(viewer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(viewer.getTeamId())).thenReturn(Optional.of(team));
            }
            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
                verify(memberRepository).findById(viewer.getMemberId());
                verify(teamRepository).findById(viewer.getTeamId());
            }
        }

        @Nested
        @DisplayName("관리자가 없는 팀 스페이스라면")
        class Context_Team_Admin_Not_Exist{
            MakeOrderRequest request;
            Team team;
            @BeforeEach
            void setUpContext(){
                request = createMakeOrderRequest(10);
                Member member = createMember(orderer.getMemberId());
                team = createTeam(orderer.getTeamId());
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.empty());
            }
            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_CREATION_NOT_ALLOWED, exception.getErrorCode());
                verify(memberRepository).findById(orderer.getMemberId());
                verify(teamRepository).findById(orderer.getTeamId());
                verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 제품이면")
        class Context_Product_Not_Exists{
            MakeOrderRequest request;
            Team team;

            @BeforeEach
            void setUpContext(){
                request = createMakeOrderRequest(10);
                Member member = createMember(orderer.getMemberId());
                team = createTeam(orderer.getTeamId());
                Participant participant = createParticipant(team);
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.of(participant));
                when(productRepository.findById(request.productId())).thenReturn(Optional.empty());
            }
            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, orderer));
                Assertions.assertEquals(InventoryErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
                verify(memberRepository).findById(orderer.getMemberId());
                verify(teamRepository).findById(orderer.getTeamId());
                verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
                verify(productRepository).findById(request.productId());
            }
        }

        @Nested
        @DisplayName("주문 수량이 제품 수량보다 많으면")
        class Context_Exceed_Amount{
            MakeOrderRequest request;
            Team team;
            @BeforeEach
            void setUpContext(){
                request = createMakeOrderRequest(1111);
                Member member = createMember(orderer.getMemberId());
                team = createTeam(orderer.getTeamId());
                Participant participant = createParticipant(team);
                Product product = Product.builder().amount(10).build();
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(Optional.of(participant));
                when(productRepository.findById(request.productId())).thenReturn(Optional.of(product));
            }

            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception(){
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_AMOUNT_EXCEED, exception.getErrorCode());
                verify(memberRepository).findById(orderer.getMemberId());
                verify(teamRepository).findById(orderer.getTeamId());
                verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
                verify(productRepository).findById(request.productId());
            }
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
        Team team = Team.builder().name("team").address("address").phoneNumber("aaa").build();
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

    private Participant createParticipant(Team team){
        Member member = Member.builder().build();
        return Participant.builder().member(member).team(team).role(MemberTeamRole.ADMIN).build();
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
