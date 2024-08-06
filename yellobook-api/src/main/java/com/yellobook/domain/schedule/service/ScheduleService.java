package com.yellobook.domain.schedule.service;


import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.CalendarResponse;
import com.yellobook.domain.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domain.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.domain.schedule.service.strategy.ScheduleStrategy;
import com.yellobook.domain.schedule.service.strategy.ScheduleStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleStrategyFactory strategyFactory;

    public UpcomingScheduleResponse getUpcomingSchedules(TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getUpcomingSchedules(teamMember);
    }

    public SearchMonthlyScheduleResponse searchMonthlySchedules(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.searchMonthlySchedules(monthlySearchParam, teamMember);
    }

    public CalendarResponse getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getCalendarSchedules(monthlyParam, teamMember);
    }

    public DailyScheduleResponse getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getDailySchedules(dailyParam, teamMember);
    }
}
