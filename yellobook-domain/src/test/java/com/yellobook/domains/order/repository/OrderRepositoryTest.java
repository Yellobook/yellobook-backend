package com.yellobook.domains.order.repository;

import static com.yellobook.common.enums.MemberTeamRole.ADMIN;
import static com.yellobook.common.enums.MemberTeamRole.ORDERER;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.support.annotation.RepositoryTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@RepositoryTest
@Sql(scripts = "classpath:setup_order.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("OrderRepository Unit Test")
public class OrderRepositoryTest {
    private final OrderRepository orderRepository;
    private final OrderCommentRepository orderCommentRepository;
    private final OrderMentionRepository orderMentionRepository;
    private final TestEntityManager entityManager;
    private final Long nonExistOrderId = 9999L;

    @Autowired
    public OrderRepositoryTest(OrderRepository orderRepository, OrderCommentRepository orderCommentRepository,
                               TestEntityManager entityManager, OrderMentionRepository orderMentionRepository) {
        this.orderRepository = orderRepository;
        this.orderCommentRepository = orderCommentRepository;
        this.orderMentionRepository = orderMentionRepository;
        this.entityManager = entityManager;
    }

    @Nested
    @DisplayName("getOrder 메소드는")
    class Describe_GetOrder {
        @Nested
        @DisplayName("존재하는 주문을 조회하면")
        class Context_order_exist {
            Long orderId = 1L;

            @Test
            @DisplayName("해당 주문을 반환한다.")
            void it_returns_order() {
                QueryOrder orderDTO = orderRepository.getOrder(orderId);

                assertThat(orderDTO).isNotNull();
                assertThat(orderDTO.memo()).isEqualTo("메모1");
            }

        }

        @Nested
        @DisplayName("존재하지 않는 주문을 조회하면")
        class Context_order_not_exist {
            Long orderId = nonExistOrderId;

            @Test
            @DisplayName("null을 반환한다.")
            void it_returns_null() {
                QueryOrder orderDTO = orderRepository.getOrder(orderId);

                assertThat(orderDTO).isNull();
            }
        }
    }

    @Nested
    @DisplayName("getOrderComments 메소드는")
    class Describe_GetOrderComments {
        @Nested
        @DisplayName("주문이 존재하지 않으면")
        class Context_order_not_exist {
            Long orderId = nonExistOrderId;

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                List<QueryOrderComment> result = orderRepository.getOrderComments(orderId);

                assertThat(result.size()).isEqualTo(0);
                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("주문이 존재하고, 주문에 댓글이 존재하지 않으면")
        class Context_ordercomment_not_exist {
            Long orderId = 2L;

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                List<QueryOrderComment> result = orderRepository.getOrderComments(orderId);

                assertThat(result.size()).isEqualTo(0);
                assertThat(result).isEmpty();
            }
        }

        @Nested
        @DisplayName("주문이 존재하고, 주문에 댓글이 존재하면")
        class Context_ordercomment_exist {
            Long orderId = 1L;

            @Test
            @DisplayName("해당 댓글들을 반환한다.")
            void it_returns_orderComments() {
                List<QueryOrderComment> result = orderRepository.getOrderComments(orderId);

                assertThat(result.size()).isEqualTo(3);
                for (int i = 1; i < result.size(); i++) {
                    LocalDateTime prev = result.get(i - 1)
                            .createdAt();
                    LocalDateTime after = result.get(i)
                            .createdAt();
                    assertThat(prev).isBeforeOrEqualTo(after);
                }
                result.forEach(comment -> assertThat(comment.role()).isIn(ADMIN, ORDERER));
                result.forEach(comment -> assertThat(getOrderCommentById(comment.commentId()).getOrder()
                        .getId()).isEqualTo(orderId));
            }
        }

        private OrderComment getOrderCommentById(Long commentId) {
            return orderCommentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("OrderComment Not Found"));
        }
    }

    @Nested
    @DisplayName("existsByMemberIdAndOrderId 메소드는")
    class Describe_ExistsByMemberIdAndOrderId {
        @Nested
        @DisplayName("주문에 언급된 관리자면")
        class Context_mentioned_admin {
            Long memberId = 1L;
            Long orderId = 1L;

            @Test
            @DisplayName("true를 반환한다.")
            void it_returns_true() {
                boolean exists = orderMentionRepository.existsByMemberIdAndOrderId(memberId, orderId);

                assertThat(exists).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("deleteAllByOrderId 메소드는")
    class Describe_DeleteAllByOrderId {
        @Nested
        @DisplayName("주문을 삭제하면")
        class Context_delete_order {
            Long orderId = 1L;

            @Test
            @DisplayName("언급된 사람도 삭제되어야 한다.")
            void it_deletes_mentioned_member() {
                orderMentionRepository.deleteAllByOrderId(orderId);
                entityManager.flush();

                boolean existsAfterDeletion = orderMentionRepository.existsByMemberIdAndOrderId(1L, orderId);
                assertThat(existsAfterDeletion).isFalse();
            }
        }
    }
}
