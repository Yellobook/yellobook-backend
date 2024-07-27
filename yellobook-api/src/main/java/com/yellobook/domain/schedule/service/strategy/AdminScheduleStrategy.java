package com.yellobook.domain.schedule.service.strategy;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO;
import com.yellobook.domain.schedule.dto.response.DailyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleDTO;
import com.yellobook.domains.schedule.dto.UpcomingScheduleDTO;
import org.springframework.stereotype.Component;

@Component
public class AdminScheduleStrategy implements  ScheduleStrategy{

    @Override
    public UpcomingScheduleDTO getUpcomingSchedules(TeamMemberVO teamMember) {
        return null;
    }

    @Override
    public SearchMonthlyScheduleDTO searchMonthlySchedules(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember) {
        return null;
    }

    @Override
    public CalendarScheduleDTO getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        return null;
    }

    @Override
    public DailyScheduleDTO getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        return null;
    }
}
