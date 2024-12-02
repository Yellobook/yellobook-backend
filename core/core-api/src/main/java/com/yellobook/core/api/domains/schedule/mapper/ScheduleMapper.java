package com.yellobook.core.api.domains.schedule.mapper;

import com.yellobook.core.domains.schedule.dto.DailyCond;
import com.yellobook.core.domains.schedule.dto.EarliestCond;
import com.yellobook.core.domains.schedule.dto.MonthlyCond;
import com.yellobook.core.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.core.domains.schedule.dto.query.QueryUpcomingSchedule;
import com.yellobook.core.api.domains.schedule.dto.request.DailyParam;
import com.yellobook.core.api.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.core.api.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.core.api.domains.schedule.dto.response.CalendarItem;
import com.yellobook.core.api.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.core.api.domains.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.core.common.vo.TeamMemberVO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "title", target = "scheduleTitle")
    UpcomingScheduleResponse toUpcomingScheduleResponse(QueryUpcomingSchedule queryUpcomingSchedule);

    @Mapping(target = "calendar", source = "calendarMap", qualifiedByName = "mapToCalendarItems")
    CalendarResponse toCalendarResponse(Map<Integer, List<String>> calendarMap);

    EarliestCond toEarliestCond(LocalDate today, TeamMemberVO teamMember);

    MonthlyCond toMonthlyCond(MonthlyParam monthlyParam, TeamMemberVO teamMember);

    SearchMonthlyCond toSearchMonthlyCond(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember);

    DailyCond toDailyCond(DailyParam dailyParam, TeamMemberVO teamMember);


    @Named("mapToCalendarItems")
    default List<CalendarItem> mapToCalendarItems(Map<Integer, List<String>> calendarMap) {
        return calendarMap.entrySet()
                .stream()
                .map(entry -> CalendarItem.builder()
                        .day(entry.getKey())
                        .titles(entry.getValue())
                        .build()
                )
                .toList();
    }
}
