package com.yellobook.domains.schedule.mapper;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.dto.DailyCond;
import com.yellobook.domains.schedule.dto.EarliestCond;
import com.yellobook.domains.schedule.dto.MonthlyCond;
import com.yellobook.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.domains.schedule.dto.request.DailyParam;
import com.yellobook.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.domains.schedule.dto.response.CalendarResponse.CalendarItem;
import com.yellobook.domains.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.UpcomingScheduleResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    EarliestCond toEarliestCond(LocalDate today, TeamMemberVO teamMember);

    MonthlyCond toMonthlyCond(MonthlyParam monthlyParam, TeamMemberVO teamMember);

    SearchMonthlyCond toSearchMonthlyCond(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember);

    DailyCond toDailyCond(DailyParam dailyParam, TeamMemberVO teamMember);

    @Mapping(source = "title", target = "scheduleTitle")
    UpcomingScheduleResponse toUpcomingScheduleResponse(QueryUpcomingSchedule queryUpcomingSchedule);

    @Mapping(source = "title", target = "scheduleTitle")
    UpcomingScheduleResponse toUpcomingScheduleResponse(String title);

    default SearchMonthlyScheduleResponse toSearchMonthlyScheduleResponse(List<QuerySchedule> schedules) {
        return new SearchMonthlyScheduleResponse(schedules);
    }

    @Mapping(source = "calendarMap", target = "calendar")
    CalendarResponse toCalendarResponse(Map<Integer, List<String>> calendarMap);

    default List<CalendarItem> toCalendarItems(Map<Integer, List<String>> calendarMap) {
        return calendarMap.entrySet()
                .stream()
                .map(entry -> new CalendarItem(entry.getKey(), entry.getValue()))
                .toList();
    }

    default DailyScheduleResponse toDailyScheduleResponse(List<QuerySchedule> schedules) {
        return new DailyScheduleResponse(schedules);
    }
}
