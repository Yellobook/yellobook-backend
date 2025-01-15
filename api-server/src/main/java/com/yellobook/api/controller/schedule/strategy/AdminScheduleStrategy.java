package com.yellobook.api.controller.schedule.strategy;

import com.yellobook.api.controller.schedule.mapper.ScheduleMapper;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.api.controller.schedule.dto.request.DailyParam;
import com.yellobook.api.controller.schedule.dto.request.MonthlyParam;
import com.yellobook.api.controller.schedule.dto.request.MonthlySearchParam;
import com.yellobook.api.controller.schedule.dto.response.CalendarResponse;
import com.yellobook.api.controller.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.api.controller.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.api.controller.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.schedule.dto.DailyCond;
import com.yellobook.schedule.dto.EarliestCond;
import com.yellobook.schedule.dto.MonthlyCond;
import com.yellobook.schedule.dto.SearchMonthlyCond;
import com.yellobook.schedule.dto.query.QueryMonthlySchedule;
import com.yellobook.schedule.dto.query.QuerySchedule;
import com.yellobook.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminScheduleStrategy implements ScheduleStrategy {
    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    public UpcomingScheduleResponse getUpcomingSchedules(TeamMemberVO teamMember) {
        LocalDate today = LocalDate.now();
        EarliestCond cond = scheduleMapper.toEarliestCond(today, teamMember);
        QueryUpcomingSchedule order = scheduleRepository.findEarliestOrder(cond)
                .orElse(null);
        QueryUpcomingSchedule inform = scheduleRepository.findEarliestInform(cond)
                .orElse(null);
        QueryUpcomingSchedule schedule = this.selectSchedule(order, inform);
        if (schedule != null) {
            return scheduleMapper.toUpcomingScheduleResponse(schedule);
        }
        return new UpcomingScheduleResponse("일정이 없습니다.");
    }


    @Override
    public SearchMonthlyScheduleResponse searchMonthlySchedules(MonthlySearchParam monthlySearchParam,
                                                                TeamMemberVO teamMember) {
        SearchMonthlyCond cond = scheduleMapper.toSearchMonthlyCond(monthlySearchParam, teamMember);
        List<QuerySchedule> orders = scheduleRepository.searchMonthlyOrders(cond);
        List<QuerySchedule> informs = scheduleRepository.searchMonthlyInforms(cond);
        List<QuerySchedule> schedules = new ArrayList<>();
        schedules.addAll(orders);
        schedules.addAll(informs);
        List<QuerySchedule> sortedSchedules = schedules.stream()
                .sorted(this::compareQuerySchedule)
                .toList();
        return new SearchMonthlyScheduleResponse(sortedSchedules);
    }


    @Override
    public CalendarResponse getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        MonthlyCond cond = scheduleMapper.toMonthlyCond(monthlyParam, teamMember);
        List<QueryMonthlySchedule> orders = scheduleRepository.findMonthlyOrders(cond);
        List<QueryMonthlySchedule> informs = scheduleRepository.findMonthlyInforms(cond);

        Map<Integer, List<String>> calendarMap = new HashMap<>();
        this.addScheduleToCalendarMap(calendarMap, orders);
        this.addScheduleToCalendarMap(calendarMap, informs);
        return scheduleMapper.toCalendarResponse(calendarMap);
    }


    @Override
    public DailyScheduleResponse getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        DailyCond cond = scheduleMapper.toDailyCond(dailyParam, teamMember);
        List<QuerySchedule> orders = scheduleRepository.findDailyOrders(cond);
        List<QuerySchedule> informs = scheduleRepository.findDailyInforms(cond);
        List<QuerySchedule> schedules = new ArrayList<>();
        schedules.addAll(orders);
        schedules.addAll(informs);
        List<QuerySchedule> sortedSchedules = schedules.stream()
                .sorted(this::compareQuerySchedule)
                .toList();
        return new DailyScheduleResponse(sortedSchedules);
    }
}