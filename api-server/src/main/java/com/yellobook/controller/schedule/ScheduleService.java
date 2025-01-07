package com.yellobook.controller.schedule;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.controller.schedule.dto.request.DailyParam;
import com.yellobook.controller.schedule.dto.request.MonthlyParam;
import com.yellobook.controller.schedule.dto.request.MonthlySearchParam;
import com.yellobook.controller.schedule.dto.response.CalendarResponse;
import com.yellobook.controller.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.controller.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.controller.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.controller.schedule.strategy.ScheduleStrategy;
import com.yellobook.controller.schedule.strategy.ScheduleStrategyFactory;
import com.yellobook.schedule.dto.query.QuerySchedule;
import java.util.List;
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

    public SearchMonthlyScheduleResponse searchMonthlySchedules(MonthlySearchParam monthlySearchParam,
                                                                TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.searchMonthlySchedules(monthlySearchParam, teamMember);
    }

    public CalendarResponse getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getCalendarSchedules(monthlyParam, teamMember);
    }

    public DailyScheduleResponse getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        DailyScheduleResponse response = strategy.getDailySchedules(dailyParam, teamMember);
        List<QuerySchedule> requiredSchedule = response.schedules()
                .stream()
                .distinct()
                .toList();
        return DailyScheduleResponse.builder()
                .schedules(requiredSchedule)
                .build();
    }
}
