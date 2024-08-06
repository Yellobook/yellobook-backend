package com.yellobook.domain.schedule.mapper;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.CalendarResponse;
import com.yellobook.domain.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.domains.schedule.dto.DailyCond;
import com.yellobook.domains.schedule.dto.EarliestCond;
import com.yellobook.domains.schedule.dto.MonthlyCond;
import com.yellobook.domains.schedule.dto.SearchMonthlyCond;
import com.yellobook.domains.schedule.dto.query.QueryUpcomingSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.yellobook.domain.schedule.dto.response.CalendarResponse.*;

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
    default List<CalendarItemDTO> mapToCalendarItems(Map<Integer, List<String>> calendarMap) {
        return calendarMap.entrySet().stream().map(entry -> CalendarItemDTO.builder().day(entry.getKey()).titles(entry.getValue()).build()).toList();
    }
}
