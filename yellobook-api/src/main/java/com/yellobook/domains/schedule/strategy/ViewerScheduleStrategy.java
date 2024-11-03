package com.yellobook.domains.schedule.strategy;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.dto.DailyCond;
import com.yellobook.domains.schedule.dto.EarliestCond;
import com.yellobook.domains.schedule.dto.MonthlyCond;
import com.yellobook.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.domains.schedule.dto.query.QueryMonthlySchedule;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.request.DailyParam;
import com.yellobook.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.domains.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.domains.schedule.mapper.ScheduleMapper;
import com.yellobook.domains.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewerScheduleStrategy implements ScheduleStrategy {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper mapper;

    @Override
    public UpcomingScheduleResponse getUpcomingSchedules(TeamMemberVO teamMember) {
        LocalDate today = LocalDate.now();
        EarliestCond cond = mapper.toEarliestCond(today, teamMember);
        return scheduleRepository.findEarliestInform(cond)
                .map(mapper::toUpcomingScheduleResponse)
                .orElseGet(() -> mapper.toUpcomingScheduleResponse("일정이 없습니다."));
    }


    @Override
    public SearchMonthlyScheduleResponse searchMonthlySchedules(MonthlySearchParam monthlySearchParam,
                                                                TeamMemberVO teamMember) {
        SearchMonthlyCond cond = mapper.toSearchMonthlyCond(monthlySearchParam, teamMember);
        List<QuerySchedule> informs = scheduleRepository.searchMonthlyInforms(cond);
        return mapper.toSearchMonthlyScheduleResponse(informs);
    }


    @Override
    public CalendarResponse getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        MonthlyCond cond = mapper.toMonthlyCond(monthlyParam, teamMember);
        List<QueryMonthlySchedule> informs = scheduleRepository.findMonthlyInforms(cond);
        Map<Integer, List<String>> calendarMap = new HashMap<>();
        this.addScheduleToCalendarMap(calendarMap, informs);
        return mapper.toCalendarResponse(calendarMap);
    }


    @Override
    public DailyScheduleResponse getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        DailyCond cond = mapper.toDailyCond(dailyParam, teamMember);
        List<QuerySchedule> informs = scheduleRepository.findDailyInforms(cond);
        return mapper.toDailyScheduleResponse(informs);
    }
}
