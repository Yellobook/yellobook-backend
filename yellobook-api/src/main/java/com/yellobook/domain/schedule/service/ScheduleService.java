package com.yellobook.domain.schedule.service;


import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO;
import com.yellobook.domain.schedule.dto.response.DailyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleDTO;
import com.yellobook.domain.schedule.service.strategy.ScheduleStrategy;
import com.yellobook.domain.schedule.service.strategy.ScheduleStrategyFactory;
import com.yellobook.domains.schedule.dto.UpcomingScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleStrategyFactory strategyFactory;

    public UpcomingScheduleDTO getUpcomingSchedules(TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getUpcomingSchedules(teamMember);
    }

    public SearchMonthlyScheduleDTO searchMonthlySchedules(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.searchMonthlySchedules(monthlySearchParam, teamMember);
    }

    public CalendarScheduleDTO getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getCalendarSchedules(monthlyParam, teamMember);
    }

    public DailyScheduleDTO getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        ScheduleStrategy strategy = strategyFactory.getStrategy(teamMember.getRole());
        return strategy.getDailySchedules(dailyParam, teamMember);
    }
}
