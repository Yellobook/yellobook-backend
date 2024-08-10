package com.yellobook.domain.order;

import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.repository.OrderCommentRepository;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.yellobook.common.enums.MemberTeamRole.ADMIN;
import static com.yellobook.common.enums.MemberTeamRole.ORDERER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaAuditing
@Sql(scripts = "classpath:setup_order.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:cleanup_order.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Order 도메인 Repository Unit Test")
public class OrderRepositoryTest {
    private final OrderRepository orderRepository;
    private final OrderCommentRepository orderCommentRepository;
    private final OrderMentionRepository orderMentionRepository;
    private final TestEntityManager entityManager;
    private final Long nonExistOrderId = 9999L;

    @Autowired
    public OrderRepositoryTest(OrderRepository orderRepository, OrderCommentRepository orderCommentRepository,
                               TestEntityManager entityManager, OrderMentionRepository orderMentionRepository){
        this.orderRepository = orderRepository;
        this.orderCommentRepository = orderCommentRepository;
        this.orderMentionRepository = orderMentionRepository;
        this.entityManager = entityManager;
    }

    @Nested
    @DisplayName("주문 조회 Test")
    class GetOrderTests{

        @Test
        @DisplayName("존재하는 주문 조회할 경우 해당 주문을 반환한다.")
        void getExistOrder(){
            //given
            Long orderId = 1L;

            //when
            QueryOrder orderDTO = orderRepository.getOrder(orderId);

            //then
            assertThat(orderDTO).isNotNull();
            //assertThat(getOrderById(orderDTO.getOrderId()).getId()).isEqualTo(orderId);
            assertThat(orderDTO.memo()).isEqualTo("메모1");
        }

        @Test
        @DisplayName("존재하지 않는 주문을 조회할 경우 null을 반환한다.")
        void getNotExistOrder(){
            //given
            Long orderId = nonExistOrderId;

            //when
            QueryOrder orderDTO = orderRepository.getOrder(orderId);

            //then
            assertThat(orderDTO).isNull();
        }

        private Order getOrderById(Long id){
            return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order Not Found"));
        }
    }

    @Nested
    @DisplayName("주문 댓글 조회 Test")
    class OrderCommentTests{
        @Test
        @DisplayName("주문에 댓글이 존재한다면 해당 댓글들을 반환한다.")
        void getOrderExistComments(){
            //given
            Long orderId = 1L;

            //when
            List<QueryOrderComment> result = orderRepository.getOrderComments(orderId);

            //then
            assertThat(result.size()).isEqualTo(3);
            for (int i = 1; i < result.size(); i++) {
                LocalDateTime prev = result.get(i-1).createdAt();
                LocalDateTime after = result.get(i).createdAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
            result.forEach(comment -> assertThat(comment.role()).isIn(ADMIN, ORDERER));
            result.forEach(comment -> assertThat(getOrderCommentById(comment.commentId()).getOrder().getId()).isEqualTo(orderId));
        }

        @Test
        @DisplayName("주문에 댓글이 존재하지 않으면 빈 리스트를 반환한다.")
        void getOrderNotExistComments(){
            //given
            Long orderId = 2L;

            //when
            List<QueryOrderComment> result = orderRepository.getOrderComments(orderId);

            //then
            assertThat(result.size()).isEqualTo(0);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("주문이 존재하지 않으면, 빈 리스트를 반환한다.")
        void getNotExistOrderComments(){
            //given
            Long orderId = nonExistOrderId;

            //when
            List<QueryOrderComment> result = orderRepository.getOrderComments(orderId);

            //then
            assertThat(result.size()).isEqualTo(0);
            assertThat(result).isEmpty();
        }

        private OrderComment getOrderCommentById(Long commentId){
            return orderCommentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("OrderComment Not Found"));
        }

    }

    @Nested
    @DisplayName("주문에 접근할 수 있는 사람 Test")
    class OrderMentionTests{

        @Test
        @DisplayName("주문에 접근할 수 있는 관리자인지 확인")
        void checkAccessToOrder(){
            //given
            Long memberId = 1L;
            Long orderId = 1L;

            //when
            boolean exists = orderMentionRepository.existsByMemberIdAndOrderId(memberId, orderId);

            //then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("주문을 삭제하고나면 언급된 사람에서 삭제되는지 확인")
        void checkOrderMentionDeleted(){
            //given
            Long orderId = 1L;

            //when
            orderMentionRepository.deleteAllByOrderId(orderId);
            entityManager.flush();

            //then
            boolean existsAfterDeletion = orderMentionRepository.existsByMemberIdAndOrderId(1L, orderId);
            assertThat(existsAfterDeletion).isFalse();
        }

    }
}
