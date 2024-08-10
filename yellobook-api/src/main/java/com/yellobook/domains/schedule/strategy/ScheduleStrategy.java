package com.yellobook.domains.schedule.strategy;

import com.yellobook.common.enums.ScheduleType;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.dto.request.DailyParam;
import com.yellobook.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.domains.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.domains.schedule.dto.query.QueryMonthlySchedule;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ScheduleStrategy {
    UpcomingScheduleResponse getUpcomingSchedules(TeamMemberVO teamMember);
    SearchMonthlyScheduleResponse searchMonthlySchedules(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember);
    CalendarResponse getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember);
    DailyScheduleResponse getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember);


    /**
     * 다가오는 일정을 선택하기 위한 메서드
     * @param order 내일부터 가장 이른 주문
     * @param inform 내일부터 가장 이른 공지 및 업무
     * @return 선택된 일정
     */
    default QueryUpcomingSchedule selectSchedule(QueryUpcomingSchedule order, QueryUpcomingSchedule inform) {
        // 둘다 존재할 경우 시간순으로 빠른 것을 표시한다.
        if (order != null && inform != null) {
            // 시간이 같을 경우 주문을 우선으로 표시한다.
            return inform.day().isBefore(order.day()) ? inform : order;
        }
        // 하나만 존재할 경우 존재하는 것을 다가오는 일정으로 표시한다.
        return order != null ? order : inform;
    }


    /**
     * 일별 일정을 종합하는 Map 에 일정을 추가하기 위한 메서드
     * @param dailyMap 일별 일정을 종합하기 위한 Map
     * @param schedules 일정 (주문, 공지 및 업무)
     */
    default void addScheduleToCalendarMap(Map<Integer, List<String>> dailyMap, List<QueryMonthlySchedule> schedules) {
        schedules.forEach(schedule -> {
            Integer day = schedule.date().getDayOfMonth();
            List<String> values = dailyMap.computeIfAbsent(day, k -> new ArrayList<>());
            // day 는 3개까지 가능
            if(values.size() < 3) {
                values.add(schedule.title());
            }
        });
    }


    /**
     * 일정을 정렬하기 위한 메서드
     * @param o1 일정
     * @param o2 일정
     * @return 정렬을 위한 정수
     * - 양수일경우 o1 이 앞으로, 0 일경우 유지, 음수일경우 o2 가 앞으로
     */
    default int compareQuerySchedule(QuerySchedule o1, QuerySchedule o2) {
        final LocalDate o1Date = o1.date();
        final LocalDate o2Date = o2.date();
        final ScheduleType o1Type = o1.scheduleType();
        final ScheduleType o2Type = o2.scheduleType();

        // 시간이 같다면 ORDER 우선
        if (o1Date.isEqual(o2Date)) {
            // 서로 다른 타입일 경우
            if (o1Type != o2Type) {
                // ORDER 우선
                return (o1Type == ScheduleType.ORDER) ? -1 : 1;
            }
            // 타입과 날짜가 모두 같다면
            return 0;
        }
        // 이전이면 음수, 이후이면 양수 반환
        return o1Date.compareTo(o2Date);
    }
}
