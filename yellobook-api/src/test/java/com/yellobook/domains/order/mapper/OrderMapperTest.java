package com.yellobook.domains.order.mapper;

import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static fixture.MemberFixture.createMember;
import static fixture.OrderFixture.createOrder;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.team.entity.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


@DisplayName("OrderMapper Unit Test")
class OrderMapperTest {
    OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Nested
    @DisplayName("toOrder 메서드는")
    class Describe_toOrder {
        @Nested
        @DisplayName("MakeOrderRequest, Member, Team, Product 를 받아")
        class Context_with_request_member_team_product_is_mapped {
            MakeOrderRequest request;
            Member member;
            Team team;
            Product product;
            final int view = 0;
            final OrderStatus orderStatus = OrderStatus.PENDING_CONFIRM;

            @BeforeEach
            void setUpContext() {
                request = MakeOrderRequest.builder()
                        .orderAmount(100)
                        .memo("주문 메모")
                        .date(LocalDate.of(2024, 11, 3))
                        .productId(1L)
                        .build();
                member = createMember();
                team = createTeam("팀1");
                Inventory inventory = createInventory(team);
                product = createProduct(inventory);
            }

            @Test
            @DisplayName("Order 객체를 반환한다.")
            void it_returns_order() {
                var target = mapper.toOrder(request, member, team, product);
                assertThat(target).isInstanceOf(Order.class);
                assertThat(target.getView()).isEqualTo(view);
                assertThat(target.getOrderAmount()).isEqualTo(request.orderAmount());
                assertThat(target.getOrderStatus()).isEqualTo(orderStatus);
                assertThat(target.getMember()
                        .getId()).isEqualTo(member.getId());
                assertThat(target.getTeam()
                        .getId()).isEqualTo(team.getId());
            }
        }
    }

    @Nested
    @DisplayName("toGetOrderCommentsResponse 메서드는")
    class Describe_toGetOrderCommentsResponse {
        @Nested
        @DisplayName("Long, List<QueryOrderComment> 를 받아")
        class Context_with_orderId_and_orderComments_is_mapped {
            final Long orderId = 1L;
            List<QueryOrderComment> orderCommentList;

            @BeforeEach
            void setUpContext() {
                orderCommentList = List.of(
                        QueryOrderComment.builder()
                                .commentId(1L)
                                .content("주문 정정 요청")
                                .role(TeamMemberRole.ORDERER)
                                .createdAt(LocalDateTime.of(2024, 11, 4, 9, 24))
                                .build(),
                        QueryOrderComment.builder()
                                .commentId(4L)
                                .content("주문 정정 확인")
                                .role(TeamMemberRole.ADMIN)
                                .createdAt(LocalDateTime.of(2024, 11, 4, 9, 24))
                                .build()
                );
            }

            @Test
            @DisplayName("GetOrderCommentsResponse 를 반환한다.")
            void it_returns_GetOrderCommentsResponse() {
                var target = mapper.toGetOrderCommentsResponse(orderId, orderCommentList);
                assertThat(target).isInstanceOf(GetOrderCommentsResponse.class);
                assertThat(target.orderId()).isEqualTo(orderId);
                var targetComments = target.comments();
                IntStream.range(0, orderCommentList.size())
                        .forEach(i -> {
                            var targetComment = targetComments.get(i);
                            var sourceComment = orderCommentList.get(i);
                            assertThat(targetComment.commentId()).isEqualTo(sourceComment.commentId());
                            assertThat(targetComment.content()).isEqualTo(sourceComment.content());
                            assertThat(targetComment.role()).isEqualTo(sourceComment.role()
                                    .getDescription());
                            assertThat(targetComment.createdAt()).isEqualTo(sourceComment.createdAt());
                        });
            }
        }
    }

    @Nested
    @DisplayName("toOrderComment 메서드는")
    class Describe_toOrderComment {
        @Nested
        @DisplayName("AddOrderCommentRequest, Member, Order 를 받아")
        class Context_with_request_member_order_is_mapped {
            AddOrderCommentRequest request;
            Member member;
            Order order;

            @BeforeEach
            void setUpContext() {
                request = AddOrderCommentRequest.builder()
                        .content("주문 수량 변경합니다.")
                        .build();
                member = createMember();
                order = createOrder(null, null, null, OrderStatus.PENDING_CONFIRM);
            }

            @Test
            @DisplayName("OrderComment를 반환한다.")
            void it_returns_OrderComment() {
                var target = mapper.toOrderComment(request, member, order);
                assertThat(target.getContent()).isEqualTo(request.content());
                assertThat(target.getMember()).isEqualTo(member);
                assertThat(target.getOrder()).isEqualTo(order);
            }
        }
    }

    @Nested
    @DisplayName("toAddOrderCommentResponse 메서드는")
    class Describe_toAddOrderCommentResponse {
        @Nested
        @DisplayName("Long을 받아")
        class Context_with_commentId_is_mapped {
            Long commentId;

            @BeforeEach
            void setUpContext() {
                commentId = 1L;
            }

            @Test
            @DisplayName("AddOrderCommentResponse를 반환한다.")
            void it_returns_AddOrderCommentResponse() {
                var target = mapper.toAddOrderCommentResponse(commentId);
                assertThat(target.commentId()).isEqualTo(commentId);
            }
        }
    }

    @Nested
    @DisplayName("toOrderMention 메서드는")
    class Describe_toOrderMention {
        @Nested
        @DisplayName("Order, Member를 받아")
        class Context_with_order_member_is_mapped {
            Order order;
            Member member;

            @BeforeEach
            void setUpContext() {
                order = createOrder(null, null, null, OrderStatus.PENDING_MODIFY);
                member = createMember();
            }

            @Test
            @DisplayName("OrderMention을 반환한다.")
            void it_returns_OrderMention() {
                var target = mapper.toOrderMention(order, member);
                assertThat(target.getOrder()).isEqualTo(order);
                assertThat(target.getMember()).isEqualTo(member);
            }
        }
    }

    @Nested
    @DisplayName("toMakeOrderResponse 메서드는")
    class Describe_toMakeOrderResponse {
        @Nested
        @DisplayName("Long을 받아")
        class Context_with_orderId_is_mapped {
            Long orderId;

            @BeforeEach
            void setUpContext() {
                orderId = 1L;
            }

            @Test
            @DisplayName("MakeOrderResponse을 반환한다.")
            void it_returns_MakeOrderResponse() {
                var target = mapper.toMakeOrderResponse(orderId);
                assertThat(target.orderId()).isEqualTo(orderId);
            }
        }
    }

    @Nested
    @DisplayName("toGetOrderResponse 메서드는")
    class Describe_toGetOrderResponse {
        @Nested
        @DisplayName("QueryOrder를 받아")
        class Context_with_queryOrder_is_mapped {
            QueryOrder queryOrder;

            @BeforeEach
            void setUpContext() {
                queryOrder = QueryOrder.builder()
                        .date(LocalDate.EPOCH)
                        .writer("작성자")
                        .productName("상품1")
                        .subProductName("노랑")
                        .amount(10)
                        .memo("메모")
                        .build();
            }

            @Test
            @DisplayName("GetOrderResponse을 반환한다.")
            void it_returns_GetOrderResponse() {
                var target = mapper.toGetOrderResponse(queryOrder);
                assertThat(target.date()).isEqualTo(queryOrder.date());
                assertThat(target.writer()).isEqualTo(queryOrder.writer());
                assertThat(target.productName()).isEqualTo(queryOrder.productName());
                assertThat(target.subProductName()).isEqualTo(queryOrder.subProductName());
                assertThat(target.amount()).isEqualTo(queryOrder.amount());
                assertThat(target.memo()).isEqualTo(queryOrder.memo());
            }
        }
    }

    @Nested
    @DisplayName("toQueryOrderComment 메서드는")
    class Describe_toQueryOrderComment {
        @Nested
        @DisplayName("QueryOrderComment를 받아")
        class Context_with_comment_is_mapped {
            QueryOrderComment comment;

            @BeforeEach
            void setUpContext() {
                comment = QueryOrderComment.builder()
                        .commentId(1L)
                        .role(TeamMemberRole.ORDERER)
                        .content("댓글내용")
                        .createdAt(LocalDateTime.of(2024, 11, 7, 10, 10))
                        .build();
            }

            @Test
            @DisplayName("CommentInfo를 반환한다.")
            void it_returns_CommentInfo() {
                var target = mapper.toQueryOrderComment(comment);
                assertThat(target.commentId()).isEqualTo(comment.commentId());
                assertThat(target.role()).isEqualTo(comment.role()
                        .getDescription());
                assertThat(target.content()).isEqualTo(comment.content());
                assertThat(target.createdAt()
                        .truncatedTo(ChronoUnit.SECONDS))
                        .isEqualTo(comment.createdAt()
                                .truncatedTo(ChronoUnit.SECONDS));
            }
        }
    }


}