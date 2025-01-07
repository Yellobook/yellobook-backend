package com.yellobook.order;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.order.OrderRepository;
import com.yellobook.core.domain.team.TeamMemberVO;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderDao implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderMentionJpaRepository orderMentionJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
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
