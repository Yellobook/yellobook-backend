package com.yellobook.core.domains.schedule.repository;

import static com.yellobook.core.domains.inform.entity.QInform.inform;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.common.vo.TeamMemberVO;
import com.yellobook.core.core.enums.ScheduleType;
import com.yellobook.core.core.enums.TeamMemberRole;
import com.yellobook.core.domains.inform.entity.QInformMention;
import com.yellobook.core.domains.order.entity.QOrder;
import com.yellobook.core.domains.schedule.dto.DailyCond;
import com.yellobook.core.domains.schedule.dto.EarliestCond;
import com.yellobook.core.domains.schedule.dto.MonthlyCond;
import com.yellobook.core.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.core.domains.schedule.dto.query.QueryMonthlySchedule;
import com.yellobook.core.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.core.domains.schedule.dto.query.QueryUpcomingSchedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleRepository {
    private final JPAQueryFactory queryFactory;
    private final StringTemplate orderTitle;

    public ScheduleRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.orderTitle = createOrderTitle();
    }

    /**
     * 다가오는 주문 제목, 날짜, 일정형식 (주문, 공지 및 일정) - 관리자면 모든 주문 - 주문자면 자신이 주문한 주문
     */
    public Optional<QueryUpcomingSchedule> findEarliestOrder(EarliestCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        LocalDate today = cond.today();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        TeamMemberRole role = teamMember.getRole();

        QueryUpcomingSchedule schedule = queryFactory
                .select(
                        Projections.constructor(QueryUpcomingSchedule.class,
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

    /**
     * 다가오는 공지/일정 제목, 날짜 일정형식 (주문 공지 및 일정 - 관리자, 주문자 구별 없이 본인이 작성했거나, 태그된 일정
     */
    public Optional<QueryUpcomingSchedule> findEarliestInform(EarliestCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        LocalDate today = cond.today();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        QueryUpcomingSchedule schedule = queryFactory
                .select(
                        Projections.constructor(QueryUpcomingSchedule.class,
                                inform.title,
                                inform.date,
                                Expressions.constant(ScheduleType.INFORM)
                        )
                )
                .from(inform)
                .leftJoin(QInformMention.informMention)
                .on(inform.id.eq(QInformMention.informMention.inform.id))
                .where(
                        inform.date.after(today),
                        inform.team.id.eq(teamId),
                        inform.member.id.eq(memberId)
                                .or(QInformMention.informMention.member.id.eq(memberId))
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetchFirst();

        return Optional.ofNullable(schedule);
    }

    /**
     * 월별 키워드에 해당하는 주문 검색
     */
    public List<QuerySchedule> searchMonthlyOrders(SearchMonthlyCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        TeamMemberRole role = teamMember.getRole();

        List<QuerySchedule> schedules = queryFactory
                .select(
                        Projections.constructor(QuerySchedule.class,
                                QOrder.order.id,
                                orderTitle.as("title"),
                                QOrder.order.date,
                                Expressions.constant(ScheduleType.ORDER)
                        )
                )
                .from(QOrder.order)
                .where(
                        eqYearMonth(QOrder.order.date, cond.year(), cond.month()),
                        eqOrderTeam(teamId),
                        eqOrderer(role, memberId),
                        orderTitle.like("%" + cond.keyword() + "%")
                )
                // 주문날짜 순으로 정렬하고, 주문날짜가 동일하면 주문글 생성시간이 빠른 순으로 한다.
                .orderBy(QOrder.order.date.asc(), QOrder.order.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 월별 키워드에 해당하는 공지/일정 검색
     */
    public List<QuerySchedule> searchMonthlyInforms(SearchMonthlyCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        List<QuerySchedule> schedules = queryFactory
                .select(
                        Projections.constructor(QuerySchedule.class,
                                inform.id,
                                inform.title,
                                inform.date,
                                Expressions.constant(ScheduleType.INFORM)
                        )
                )
                .from(inform)
                .leftJoin(QInformMention.informMention)
                .on(inform.id.eq(QInformMention.informMention.inform.id))
                .where(
                        eqYearMonth(inform.date, cond.year(), cond.month()),
                        inform.team.id.eq(teamId),
                        inform.member.id.eq(memberId)
                                .or(QInformMention.informMention.member.id.eq(memberId)),
                        inform.title.like("%" + cond.keyword() + "%")
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 월별 종합 주문
     */
    public List<QueryMonthlySchedule> findMonthlyOrders(MonthlyCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        TeamMemberRole role = teamMember.getRole();

        List<QueryMonthlySchedule> schedules = queryFactory
                .select(
                        Projections.constructor(QueryMonthlySchedule.class,
                                QOrder.order.id,
                                orderTitle.as("title"),
                                QOrder.order.date,
                                QOrder.order.createdAt,
                                Expressions.constant(ScheduleType.ORDER)
                        )
                )
                .from(QOrder.order)
                .where(
                        eqYearMonth(QOrder.order.date, cond.year(), cond.month()),
                        eqOrderTeam(teamId),
                        eqOrderer(role, memberId)
                )
                .orderBy(QOrder.order.date.asc(), QOrder.order.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 월별 종합 공지/일정
     */
    public List<QueryMonthlySchedule> findMonthlyInforms(MonthlyCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        List<QueryMonthlySchedule> schedules = queryFactory
                .select(
                        Projections.constructor(QueryMonthlySchedule.class,
                                inform.id,
                                inform.title,
                                inform.date,
                                inform.createdAt,
                                Expressions.constant(ScheduleType.INFORM)
                        )
                )
                .from(inform)
                .leftJoin(QInformMention.informMention)
                .on(inform.id.eq(QInformMention.informMention.inform.id))
                .where(
                        eqYearMonth(inform.date, cond.year(), cond.month()),
                        inform.team.id.eq(teamId),
                        inform.member.id.eq(memberId)
                                .or(QInformMention.informMention.member.id.eq(memberId))
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetch();
        return schedules;
    }


    /**
     * 날짜별 주문 조회
     */
    public List<QuerySchedule> findDailyOrders(DailyCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        TeamMemberRole role = teamMember.getRole();

        List<QuerySchedule> schedules = queryFactory
                .select(
                        Projections.constructor(QuerySchedule.class,
                                QOrder.order.id,
                                orderTitle.as("title"),
                                QOrder.order.date,
                                Expressions.constant(ScheduleType.INFORM)
                        )
                )
                .from(QOrder.order)
                .where(
                        // 해당 월의 주문이여야 하고
                        eqYearMonthDay(QOrder.order.date, cond.year(), cond.month(), cond.day()),
                        // 해당 팀의 주문이어야 하고
                        eqOrderTeam(teamId),
                        // 주문자라면 자신이 주문한 주문이어야 하고
                        eqOrderer(role, memberId)
                )
                // 주문날짜 순으로 정렬하고, 주문날짜가 동일하면 주문글 생성시간이 빠른 순으로
                .orderBy(QOrder.order.date.asc(), QOrder.order.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 날짜별 공지/일정 조회
     */
    public List<QuerySchedule> findDailyInforms(DailyCond cond) {
        TeamMemberVO teamMember = cond.teamMember();
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        List<QuerySchedule> schedules = queryFactory
                .select(
                        Projections.constructor(QuerySchedule.class,
                                inform.id,
                                inform.title,
                                inform.date,
                                Expressions.constant(ScheduleType.INFORM)
                        )
                )
                .from(inform)
                .leftJoin(QInformMention.informMention)
                .on(inform.id.eq(QInformMention.informMention.inform.id))
                .where(
                        eqYearMonthDay(inform.date, cond.year(), cond.month(), cond.day()),
                        inform.member.id.eq(memberId)
                                .or(QInformMention.informMention.member.id.eq(memberId))
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetch();
        return schedules;
    }


    private StringTemplate createOrderTitle() {
        return Expressions.stringTemplate(
                "concat({0}, ' ', {1}, ' ', {2}, '개')",
                QOrder.order.product.name, QOrder.order.product.subProduct, QOrder.order.orderAmount
        );
    }

    private BooleanExpression eqYearMonth(DatePath<LocalDate> datePath, int year, int month) {
        return datePath.year()
                .eq(year)
                .and(datePath.month()
                        .eq(month));
    }

    private BooleanExpression eqYearMonthDay(DatePath<LocalDate> datePath, int year, int month, int day) {
        return datePath.year()
                .eq(year)
                .and(datePath.month()
                        .eq(month))
                .and(datePath.dayOfMonth()
                        .eq(day));
    }

    /**
     * 주문자라면 자신이 작성한 주문이어야 한다.
     */
    private BooleanExpression eqOrderer(TeamMemberRole role, Long memberId) {
        if (role == TeamMemberRole.ORDERER) {
            return QOrder.order.member.id.eq(memberId);
        }
        return null;
    }

    private BooleanExpression eqOrderTeam(Long teamId) {
        return QOrder.order.team.id.eq(teamId);
    }
}