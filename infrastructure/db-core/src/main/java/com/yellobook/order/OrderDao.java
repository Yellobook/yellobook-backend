package com.yellobook.order;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.order.Order;
import com.yellobook.core.domain.order.OrderRepository;
import com.yellobook.core.domain.order.OrderStatus;
import com.yellobook.core.domain.order.dto.CreateOrderCommend;
import com.yellobook.core.domain.team.TeamMemberVO;
import com.yellobook.inventory.ProductEntity;
import com.yellobook.inventory.ProductJpaRepository;
import com.yellobook.member.MemberEntity;
import com.yellobook.member.MemberJpaRepository;
import com.yellobook.team.TeamEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TeamJpaRepository teamJpaRepository; // TeamJpaRepository
    private final JPAQueryFactory queryFactory;

    @Autowired
    public OrderDao(OrderJpaRepository orderJpaRepository,
                    ProductJpaRepository productJpaRepository, MemberJpaRepository memberJpaRepository,
                    TeamJpaRepository teamRepository, JPAQueryFactory queryFactory) {
        this.orderJpaRepository = orderJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.teamRepository = teamRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(CreateOrderCommend order) {
        ProductEntity product = productJpaRepository.getReferenceById(order.productId());
        MemberEntity member = memberJpaRepository.getReferenceById(order.ordererId());
        TeamEntity team = teamJpaRepository.getReferenceById(order.teamId());
        OrderEntity orderEntity = OrderEntity.builder()
                .view(order.view())
                .memo(order.memo())
                .date(order.date())
                .orderStatus(order.status())
                .orderAmount(order.orderAmount())
                .product(product)
                .member(member)
                .team(team)
                .build();
        return orderJpaRepository.save(orderEntity)
                .getId();
    }

    @Override
    public void updateOrderStatus(OrderStatus status, Long orderId) {
        orderJpaRepository.updateOrderStatus(status, orderId);
    }

    @Override
    public void delete(Long orderId) {
        orderJpaRepository.deleteById(orderId);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        Optional<OrderEntity> orderEntity = orderJpaRepository.findById(orderId);
        return orderEntity.map(OrderEntity::toOrder);
    }

    @Override
    public boolean existByProductId(Long productId) {
        return orderJpaRepository.existsByProductId(productId);
    }


    public QueryOrder getOrder(Long orderId) {
        QOrder order = QOrder.order;

        return queryFactory.select(Projections.constructor(QueryOrder.class,
                        order.date,
                        order.member.nickname.as("writer"),
                        order.product.name.as("productName"),
                        order.product.subProduct.as("subProductName"),
                        order.orderAmount.as("amount"),
                        order.memo
                ))
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();
    }

    /**
     * 다가오는 주문 제목, 날짜, 일정형식 (주문, 공지 및 일정) - 관리자면 모든 주문 - 주문자면 자신이 주문한 주문
     */
    public Optional<com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule> findEarliestUpcomingOrder(
            com.yellobook.domains.schedule.dto.EarliestCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        LocalDate today = cond.today();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        TeamMemberRole role = teamMember.getRole();

        com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule schedule = queryFactory
                .select(
                        Projections.constructor(com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule.class,
                                orderTitle.as("title"),
                                QOrder.order.date,
                                Expressions.constant(ScheduleType.ORDER)
                        )
                )
                .from(QOrder.order)
                .where(
                        QOrder.order.date.after(today),
                        eqOrderTeam(teamId),
                        eqOrderer(role, memberId)
                )
                .orderBy(QOrder.order.date.asc(), QOrder.order.createdAt.asc())
                // 첫 번째 결과를 가져온다.
                .fetchFirst();
        return Optional.ofNullable(schedule);
    }


}
