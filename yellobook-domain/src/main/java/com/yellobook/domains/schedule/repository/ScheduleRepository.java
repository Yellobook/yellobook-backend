package com.yellobook.domains.schedule.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.dto.MonthlyScheduleDTO;
import com.yellobook.domains.schedule.dto.ScheduleDTO;
import com.yellobook.domains.schedule.dto.UpcomingScheduleDTO;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.ScheduleType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static com.yellobook.domains.inform.entity.QInform.*;
import static com.yellobook.domains.inform.entity.QInformMention.*;
import static com.yellobook.domains.order.entity.QOrder.*;


import java.util.List;

@Repository
public class ScheduleRepository {
    private final JPAQueryFactory queryFactory;
    private final StringTemplate orderTitle;

    public ScheduleRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.orderTitle = createOrderTitle();
    }

    /**
     * 다가오는 주문
     * 제목, 날짜, 일정형식 (주문, 공지 및 일정)
     * - 관리자면 모든 주문
     * - 주문자면 자신이 주문한 주문
     */
    public Optional<UpcomingScheduleDTO> findEarliestOrder(LocalDate today, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        MemberTeamRole role = teamMember.getRole();

        UpcomingScheduleDTO schedule = queryFactory
                .select(
                        Projections.constructor(UpcomingScheduleDTO.class,
                                orderTitle.as("title"),
                                order.date,
                                stringTemplate("{0}", ScheduleType.ORDER.getValue()).as("scheduleType")
                        )
                )
                .from(order)
                .where(
                        order.date.after(today),
                        eqOrderTeam(teamId),
                        eqOrderer(role, memberId)
                )
                .orderBy(order.date.asc(), order.createdAt.asc())
                // 첫 번째 결과를 가져온다.
                .fetchFirst();
        return Optional.ofNullable(schedule);
    }

    /**
     * 다가오는 공지/일정
     * 제목, 날짜 일정형식 (주문 공지 및 일정
     * - 관리자, 주문자 구별 없이 본인이 작성했거나, 태그된 일정
     */
    public Optional<UpcomingScheduleDTO> findEarliestInform(LocalDate today, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        UpcomingScheduleDTO schedule = queryFactory
                .select(
                        Projections.constructor(UpcomingScheduleDTO.class,
                                inform.title,
                                inform.date,
                                stringTemplate("{0}", ScheduleType.INFORM.getValue()).as("scheduleType")
                        )
                )
                .from(inform)
                .leftJoin(informMention)
                .on(inform.id.eq(informMention.inform.id))
                .where(
                        inform.date.after(today),
                        inform.team.id.eq(teamId),
                        inform.member.id.eq(memberId).or(informMention.member.id.eq(memberId))
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetchFirst();

        return Optional.ofNullable(schedule);
    }

    /**
     * 월별 키워드에 해당하는 주문 검색
     */
    public List<ScheduleDTO> searchMonthlyOrders(String keyword, int year, int month, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        MemberTeamRole role = teamMember.getRole();

        List<ScheduleDTO> schedules = queryFactory
                .select(
                        Projections.constructor(ScheduleDTO.class,
                                order.id,
                                orderTitle.as("title"),
                                order.date,
                                stringTemplate("{0}", ScheduleType.ORDER.getValue()).as("scheduleType")
                        )
                )
                .from(order)
                .where(
                        eqYearMonth(order.date, year, month),
                        eqOrderTeam(teamId),
                        eqOrderer(role, memberId),
                        orderTitle.like("%" + keyword + "%")
                )
                // 주문날짜 순으로 정렬하고, 주문날짜가 동일하면 주문글 생성시간이 빠른 순으로 한다.
                .orderBy(order.date.asc(), order.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 월별 키워드에 해당하는 공지/일정 검색
     */
    public List<ScheduleDTO> searchMonthlyInforms(String keyword, int year, int month, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        List<ScheduleDTO> schedules = queryFactory
                .select(
                        Projections.constructor(ScheduleDTO.class,
                                inform.id,
                                inform.title,
                                inform.date,
                                stringTemplate("{0}", ScheduleType.INFORM.getValue()).as("scheduleType")
                        )
                )
                .from(inform)
                .leftJoin(informMention)
                .on(inform.id.eq(informMention.inform.id))
                .where(
                        eqYearMonth(inform.date, year, month),
                        inform.team.id.eq(teamId),
                        inform.member.id.eq(memberId).or(informMention.member.id.eq(memberId)),
                        inform.title.like("%" + keyword + "%")
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 월별 종합 주문
     */
    public List<MonthlyScheduleDTO> findMonthlyOrders(int year, int month, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        MemberTeamRole role = teamMember.getRole();

        List<MonthlyScheduleDTO> schedules = queryFactory
                .select(
                        Projections.constructor(MonthlyScheduleDTO.class,
                                order.id,
                                orderTitle.as("title"),
                                order.date,
                                order.createdAt,
                                stringTemplate("{0}", ScheduleType.ORDER.getValue()).as("scheduleType")
                        )
                )
                .from(order)
                .where(
                        eqYearMonth(order.date, year, month),
                        eqOrderTeam(teamId),
                        eqOrderer(role, memberId)
                )
                .orderBy(order.date.asc(), order.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 월별 종합 공지/일정
     */
    public List<MonthlyScheduleDTO> findMonthlyInforms(int year, int month, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        List<MonthlyScheduleDTO> schedules = queryFactory
                .select(
                        Projections.constructor(MonthlyScheduleDTO.class,
                                inform.id,
                                inform.title,
                                inform.date,
                                inform.createdAt,
                                stringTemplate("{0}", ScheduleType.INFORM.getValue()).as("scheduleType")
                        )
                )
                .from(inform)
                .leftJoin(informMention)
                .on(inform.id.eq(informMention.inform.id))
                .where(
                        eqYearMonth(inform.date, year, month),
                        inform.team.id.eq(teamId),
                        inform.member.id.eq(memberId).or(informMention.member.id.eq(memberId))
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetch();
        return schedules;
    }


    /**
     * 날짜별 주문 조회
     */
    public List<ScheduleDTO> findDailyOrders(int year, int month, int day, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();
        MemberTeamRole role = teamMember.getRole();

        List<ScheduleDTO> schedules = queryFactory
                .select(
                        Projections.constructor(ScheduleDTO.class,
                                order.id,
                                orderTitle.as("title"),
                                order.date,
                                stringTemplate("{0}", ScheduleType.ORDER.getValue()).as("scheduleType")
                        )
                )
                .from(order)
                .where(
                        // 해당 월의 주문이여야 하고
                        eqYearMonthDay(order.date, year, month, day),
                        // 해당 팀의 주문이어야 하고
                        eqOrderTeam(teamId),
                        // 주문자라면 자신이 주문한 주문이어야 하고
                        eqOrderer(role, memberId)
                )
                // 주문날짜 순으로 정렬하고, 주문날짜가 동일하면 주문글 생성시간이 빠른 순으로
                .orderBy(order.date.asc(), order.createdAt.asc())
                .fetch();
        return schedules;
    }

    /**
     * 날짜별 공지/일정 조회
     */
    public List<ScheduleDTO> findDailyInforms(int year, int month, int day, TeamMemberVO teamMember) {
        Long teamId = teamMember.getTeamId();
        Long memberId = teamMember.getMemberId();

        List<ScheduleDTO> schedules = queryFactory
                .select(
                        Projections.constructor(ScheduleDTO.class,
                                inform.id,
                                inform.title,
                                inform.date,
                                stringTemplate("{0}", ScheduleType.INFORM.getValue()).as("scheduleType")
                        )
                )
                .from(inform)
                .leftJoin(informMention)
                .on(inform.id.eq(informMention.inform.id))
                .where(
                        eqYearMonthDay(inform.date, year, month, day),
                        inform.member.id.eq(memberId).or(informMention.member.id.eq(memberId))
                )
                .orderBy(inform.date.asc(), inform.createdAt.asc())
                .fetch();
        return schedules;
    }


    private StringTemplate createOrderTitle() {
        return stringTemplate(
                "concat({0}, ' ', {1}, ' ', {2}, '개')",
                order.product.name, order.product.subProduct, order.orderAmount
        );
    }

    private BooleanExpression eqYearMonth(DatePath<LocalDate> datePath, int year, int month) {
        return datePath.year().eq(year)
                .and(datePath.month().eq(month));
    }

    private BooleanExpression eqYearMonthDay(DatePath<LocalDate> datePath, int year, int month, int day) {
        return datePath.year().eq(year)
                .and(datePath.month().eq(month))
                .and(datePath.dayOfMonth().eq(day));
    }

    /**
     * 주문자라면 자신이 작성한 주문이어야 한다.
     */
    private BooleanExpression eqOrderer(MemberTeamRole role, Long memberId) {
        if (role == MemberTeamRole.ORDERER) {
            return order.member.id.eq(memberId);
        }
        return null;
    }

    private BooleanExpression eqOrderTeam(Long teamId) {
        return order.team.id.eq(teamId);
    }
}