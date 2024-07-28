package com.yellobook.domain.schedule.mapper;

import com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO;
import com.yellobook.domain.schedule.dto.response.UpcomingScheduleDTO;
import com.yellobook.domains.schedule.dto.QueryUpcomingScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Map;

import static com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO.*;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "title", target = "scheduleTitle")
    UpcomingScheduleDTO toUpcomingScheduleDTO(QueryUpcomingScheduleDTO queryUpcomingScheduleDTO);

    @Mapping(target = "calendar", source = "calendarMap", qualifiedByName = "mapToCalendarItems")
    CalendarScheduleDTO mapToCalendarScheduleDTO(Map<Integer, List<String>> calendarMap);

    @Named("mapToCalendarItems")
    default List<CalendarItemDTO> mapToCalendarItems(Map<Integer, List<String>> calendarMap) {
        return calendarMap.entrySet().stream()
                .map(entry -> CalendarItemDTO.builder()
                        .day(entry.getKey())
                        .titles(entry.getValue())
                        .build()
                )
                .toList();
    }
}
