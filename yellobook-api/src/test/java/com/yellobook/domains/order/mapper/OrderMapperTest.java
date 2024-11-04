package com.yellobook.domains.order.mapper;

import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.team.entity.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


}