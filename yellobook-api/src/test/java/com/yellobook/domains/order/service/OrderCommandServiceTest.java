package com.yellobook.domains.order.service;

import static fixture.InventoryFixture.createProduct;
import static fixture.MemberFixture.createMember;
import static fixture.OrderFixture.createOrder;
import static fixture.OrderFixture.createOrderComment;
import static fixture.TeamFixture.createParticipant;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.lang.reflect.Field;
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
    class Describe_ModifyRequestOrder {
        @Nested
        @DisplayName("해당 주문에 언급된 관리자가 아니면")
        class Context_not_order_admin {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                order = createOrder(null, null, null, OrderStatus.PENDING_CONFIRM);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        false);
            }

            @Test
            @DisplayName("주문에 접근하지 못하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.modifyRequestOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("주문 확인 상태이면")
        class Context_order_confirmed {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                order = createOrder(null, null, null, OrderStatus.CONFIRMED);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        true);
            }

            @Test
            @DisplayName("주문 정정 요청이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.modifyRequestOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_CONFIRMED_CANT_MODIFY, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("주문 정정 요청이 가능하면")
        class Context_can_request_modify_order {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                order = createOrder(null, null, null, OrderStatus.PENDING_CONFIRM);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        true);
            }

            @Test
            @DisplayName("주문 정정 요청을 수행한다.")
            void it_request_modify_order() {
                orderCommandService.modifyRequestOrder(orderId, admin);

                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING_MODIFY);
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

    }

    @Nested
    @DisplayName("confirmOrder 메소드는")
    class Describe_ConfirmOrder {
        @Nested
        @DisplayName("해당 주문에 언급된 관리자가 아니면")
        class Context_not_order_admin {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                order = createOrder(null, null, null, OrderStatus.PENDING_CONFIRM);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        false);
            }

            @Test
            @DisplayName("해당 주문에 접근할 수 없기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.confirmOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("주문 정정 요청 상태이면")
        class Context_order_pending_modify {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                order = createOrder(null, null, null, OrderStatus.PENDING_MODIFY);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        true);
            }

            @Test
            @DisplayName("주문 확정이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.confirmOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_PENDING_MODIFY_CANT_CONFIRM, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }

        @Nested
        @DisplayName("현재 재고 수량보다 많은 수량의 주문이면")
        class Context_exceed_amount {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Product product = createProduct(null, 10);
                order = createOrder(null, null, product, OrderStatus.PENDING_CONFIRM, 100);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        true);
            }

            @Test
            @DisplayName("주문 확정이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.confirmOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_AMOUNT_EXCEED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }


        @Nested
        @DisplayName("주문 확정이 가능하면")
        class Context_can_confirm_order {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Product product = createProduct(null, 100);
                order = createOrder(null, null, product, OrderStatus.PENDING_CONFIRM, 10);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(orderMentionRepository.existsByMemberIdAndOrderId(admin.getMemberId(), order.getId())).thenReturn(
                        true);
            }

            @Test
            @DisplayName("주문 확정을 수행한다.")
            void it_confirms_order() {
                orderCommandService.confirmOrder(orderId, admin);

                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CONFIRMED);
                assertThat(order.getProduct()
                        .getAmount()).isEqualTo(100 - 10);
                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).existsByMemberIdAndOrderId(admin.getMemberId(), order.getId());
            }
        }
    }

    @Nested
    @DisplayName("cancelOrder 메소드는")
    class Describe_CancelOrder {
        @Nested
        @DisplayName("내가 작성한 주문이 아니면")
        class Context_not_my_order {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(2L);
                Order order = createOrder(null, member, null, OrderStatus.PENDING_MODIFY);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("취소가 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.cancelOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
            }
        }

        @Nested
        @DisplayName("주문 정정 상태가 아니면")
        class Context_order_not_pending_modify {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(1L);
                Order order = createOrder(null, member, null, OrderStatus.CONFIRMED);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("취소가 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.cancelOrder(orderId, admin));
                Assertions.assertEquals(OrderErrorCode.ORDER_CANT_CANCEL, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
            }
        }

        @Nested
        @DisplayName("주문 취소가 가능하면")
        class Context_can_cancel_order {
            Long orderId;
            Order order;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member member = createMemberWithId(1L);
                order = createOrder(null, member, null, OrderStatus.PENDING_MODIFY);
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("주문을 취소한다.")
            void it_cancel_order() {
                orderCommandService.cancelOrder(orderId, admin);

                verify(orderRepository).findById(orderId);
                verify(orderMentionRepository).deleteAllByOrderId(order.getId());
                verify(orderRepository).delete(order);
            }
        }
    }

    @Nested
    @DisplayName("addOrderComment 메소드는")
    class Describe_AddOrderComment {
        @Nested
        @DisplayName("해당 주문의 주문자가 아니면")
        class Context_not_order_orderer {
            Long orderId;
            AddOrderCommentRequest request;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member orderMember = createMemberWithId(1L);
                Order order = createOrder(null, orderMember, null, OrderStatus.PENDING_CONFIRM); // 1L
                Member member = createMemberWithId(orderer.getMemberId());  // member : 2L
                request = createAddOrderCommentRequest();
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())).thenReturn(
                        false);
            }

            @Test
            @DisplayName("댓글 작성이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.addOrderComment(orderId, orderer, request));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(memberRepository).findById(orderer.getMemberId());
            }
        }

        @Nested
        @DisplayName("해당 주문에 언급된 관리자가 아니면")
        class Context_not_order_admin {
            Long orderId;
            AddOrderCommentRequest request;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member orderMember = createMemberWithId(2L);
                Order order = createOrder(null, orderMember, null, OrderStatus.PENDING_CONFIRM); // 2L
                Member member = createMemberWithId(admin.getMemberId());  // member : 1L
                request = createAddOrderCommentRequest();
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
                when(orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())).thenReturn(
                        false);
            }

            @Test
            @DisplayName("댓글 작성이 불가능하기 때문에 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.addOrderComment(orderId, admin, request));
                Assertions.assertEquals(OrderErrorCode.ORDER_ACCESS_DENIED, exception.getErrorCode());
                verify(orderRepository).findById(orderId);
                verify(memberRepository).findById(admin.getMemberId());
            }
        }

        @Nested
        @DisplayName("해당 주문의 주문자가 맞으면")
        class Context_order_orderer {
            Long orderId;
            Long commentId;
            AddOrderCommentRequest request;
            AddOrderCommentResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
                Member orderMember = createMemberWithId(1L);
                Order order = createOrder(null, orderMember, null, OrderStatus.PENDING_CONFIRM); // 1L
                Member member = createMemberWithId(admin.getMemberId());  // member : 1L
                request = createAddOrderCommentRequest();
                OrderComment comment = createOrderCommentWithId(1L);
                commentId = 1L;
                expectResponse = AddOrderCommentResponse.builder()
                        .commentId(1L)
                        .build();
                when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
                when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
                when(orderMapper.toOrderComment(request, member, order)).thenReturn(comment);
                when(orderCommentRepository.save(any(OrderComment.class))).thenReturn(comment);
                when(orderMapper.toAddOrderCommentResponse(commentId)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("주문에 댓글을 추가한다.")
            void it_adds_order_comment() {
                AddOrderCommentResponse response = orderCommandService.addOrderComment(orderId, admin, request);

                assertThat(response).isNotNull();
                assertThat(response.commentId()).isEqualTo(expectResponse.commentId());
                verify(orderRepository).findById(orderId);
                verify(memberRepository).findById(admin.getMemberId());
                verify(orderCommentRepository).save(any(OrderComment.class));
                verify(orderMapper).toAddOrderCommentResponse(commentId);
            }
        }

        private AddOrderCommentRequest createAddOrderCommentRequest() {
            return AddOrderCommentRequest.builder()
                    .content("댓글 내용")
                    .build();
        }
    }

    @Nested
    @DisplayName("makeOrder 메소드는")
    class Describe_MakeOrder {
        @Nested
        @DisplayName("관리자라면")
        class Context_admin {
            MakeOrderRequest request;

            @BeforeEach
            void setUpContext() {
                request = createMakeOrderRequest(10);
                Member member = createMemberWithId(admin.getMemberId());
                Team team = createTeamWithId(admin.getTeamId());
                when(memberRepository.findById(admin.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(admin.getTeamId())).thenReturn(Optional.of(team));
            }

            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, admin));
                Assertions.assertEquals(AuthErrorCode.ADMIN_NOT_ALLOWED, exception.getErrorCode());
                verify(memberRepository).findById(admin.getMemberId());
                verify(teamRepository).findById(admin.getTeamId());
            }
        }

        @Nested
        @DisplayName("뷰어라면")
        class Context_viewer {
            MakeOrderRequest request;

            @BeforeEach
            void setUpContext() {
                request = createMakeOrderRequest(10);
                Member member = createMemberWithId(viewer.getMemberId());
                Team team = createTeamWithId(viewer.getTeamId());
                when(memberRepository.findById(viewer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(viewer.getTeamId())).thenReturn(Optional.of(team));
            }

            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
                verify(memberRepository).findById(viewer.getMemberId());
                verify(teamRepository).findById(viewer.getTeamId());
            }
        }

        @Nested
        @DisplayName("관리자가 없는 팀 스페이스라면")
        class Context_team_admin_not_exist {
            MakeOrderRequest request;
            Team team;

            @BeforeEach
            void setUpContext() {
                request = createMakeOrderRequest(10);
                Member member = createMemberWithId(orderer.getMemberId());
                team = createTeamWithId(orderer.getTeamId());
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(
                        Optional.empty());
            }

            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
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
        class Context_product_not_exists {
            MakeOrderRequest request;
            Team team;

            @BeforeEach
            void setUpContext() {
                request = createMakeOrderRequest(10);
                Member member = createMemberWithId(orderer.getMemberId());
                team = createTeamWithId(orderer.getTeamId());
                Participant participant = createParticipant(team, member, MemberTeamRole.ADMIN);
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(
                        Optional.of(participant));
                when(productRepository.findById(request.productId())).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
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
        class Context_exceed_amount {
            MakeOrderRequest request;
            Team team;

            @BeforeEach
            void setUpContext() {
                request = createMakeOrderRequest(1111);
                Member member = createMemberWithId(orderer.getMemberId());
                team = createTeamWithId(orderer.getTeamId());
                Participant participant = createParticipant(team, member, MemberTeamRole.ADMIN);
                Product product = createProduct(null);
                when(memberRepository.findById(orderer.getMemberId())).thenReturn(Optional.of(member));
                when(teamRepository.findById(orderer.getTeamId())).thenReturn(Optional.of(team));
                when(participantRepository.findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN)).thenReturn(
                        Optional.of(participant));
                when(productRepository.findById(request.productId())).thenReturn(Optional.of(product));
            }

            @Test
            @DisplayName("주문 생성이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        orderCommandService.makeOrder(request, orderer));
                Assertions.assertEquals(OrderErrorCode.ORDER_AMOUNT_EXCEED, exception.getErrorCode());
                verify(memberRepository).findById(orderer.getMemberId());
                verify(teamRepository).findById(orderer.getTeamId());
                verify(participantRepository).findByTeamIdAndRole(team.getId(), MemberTeamRole.ADMIN);
                verify(productRepository).findById(request.productId());
            }
        }

        private MakeOrderRequest createMakeOrderRequest(Integer orderAmount) {
            return MakeOrderRequest.builder()
                    .productId(1L)
                    .orderAmount(orderAmount)
                    .build();
        }
    }


    private Member createMemberWithId(Long memberId) {
        Member member = createMember();
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

    private Team createTeamWithId(Long teamId) {
        Team team = createTeam();
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

    private OrderComment createOrderCommentWithId(Long commentId) {
        OrderComment comment = createOrderComment(null, null);
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
