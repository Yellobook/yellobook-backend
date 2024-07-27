package com.yellobook.domain.schedule.service.strategy;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO;
import com.yellobook.domain.schedule.dto.response.DailyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleDTO;
import com.yellobook.domains.schedule.dto.UpcomingScheduleDTO;

public interface ScheduleStrategy {
    UpcomingScheduleDTO getUpcomingSchedules(TeamMemberVO teamMember);
    SearchMonthlyScheduleDTO searchMonthlySchedules(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember);
    CalendarScheduleDTO getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember);
    DailyScheduleDTO getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember);
}
