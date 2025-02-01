//package com.yellobook.storage.db.core.order;
//
//import com.querydsl.core.types.Projections;
//import com.querydsl.core.types.dsl.Expressions;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.yellobook.core.domain.order.OrderRepository;
//import com.yellobook.core.domain.team.TeamMemberVO;
//import com.yellobook.storage.db.core.member.MemberEntity;
//import com.yellobook.storage.db.core.schedule.dto.EarliestCond;
//import com.yellobook.storage.db.core.schedule.dto.query.QueryUpcomingSchedule;
//import java.time.LocalDate;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class OrderDao implements OrderRepository {
//    private final OrderJpaRepository orderJpaRepository;
//    private final OrderMentionJpaRepository orderMentionJpaRepository;
//    private final JPAQueryFactory queryFactory;
//
//    // 생성한다고 하면
//    public Long save(CreateOrderCommend) {
//        // Order 를 new 로 생성
//        OrderEntity order = new OrderEntity(값들 넣어주고);
//        MemberEntity member = findXXX (또는 queryDSL);
//        order.member = member;
//        orderJpaRepository.save(order);
//        //
//        // 서비스에서 member 도메인객체를 memberReader 로 읽어와서 넘겨주면
//        // 여기서 new MmeberEntity -> memberEntity 가 있어야 함
//
//        // 영속성컨텍스트에서 ne
//
//
//        // @Transactional 묶여있어야 같은 영속성 컨텍스트인데
//        // Resolver 에서 조회를 해옴 -> 영속성컨텍스트에 존재할까? 사용한다면
//        // id 필드
//    }
//
//
//
//    @Override
//    public QueryOrder getOrder(Long orderId) {
//        QOrder order = QOrder.order;
//
//        return queryFactory.select(Projections.constructor(QueryOrder.class,
//                        order.date,
//                        order.member.nickname.as("writer"),
//                        order.product.name.as("productName"),
//                        order.product.subProduct.as("subProductName"),
//                        order.orderAmount.as("amount"),
//                        order.memo
//                ))
//                .from(order)
//                .where(order.id.eq(orderId))
//                .fetchOne();
//    }
//
//    /**
//     * 다가오는 주문 제목, 날짜, 일정형식 (주문, 공지 및 일정) - 관리자면 모든 주문 - 주문자면 자신이 주문한 주문
//     */
//    public Optional<QueryUpcomingSchedule> findEarliestUpcomingOrder(
//            EarliestCond cond) {
//        TeamMemberVO teamMember = cond.teamMember();
//        LocalDate today = cond.today();
//        Long teamId = teamMember.getTeamId();
//        Long memberId = teamMember.getMemberId();
//        TeamMemberRole role = teamMember.getRole();
//
//        QueryUpcomingSchedule schedule = queryFactory
//                .select(
//                        Projections.constructor(QueryUpcomingSchedule.class,
//                                orderTitle.as("title"),
//                                QOrder.order.date,
//                                Expressions.constant(ScheduleType.ORDER)
//                        )
//                )
//                .from(QOrder.order)
//                .where(
//                        QOrder.order.date.after(today),
//                        eqOrderTeam(teamId),
//                        eqOrderer(role, memberId)
//                )
//                .orderBy(QOrder.order.date.asc(), QOrder.order.createdAt.asc())
//                // 첫 번째 결과를 가져온다.
//                .fetchFirst();
//        return Optional.ofNullable(schedule);
//    }
//}
