package com.yellobook.domains.schedule;


import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.request.DailyParam;
import com.yellobook.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.domains.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.domains.schedule.strategy.ScheduleStrategy;
import com.yellobook.domains.schedule.strategy.ScheduleStrategyFactory;
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
