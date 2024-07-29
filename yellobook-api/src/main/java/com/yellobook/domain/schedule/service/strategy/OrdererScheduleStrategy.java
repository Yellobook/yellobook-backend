package com.yellobook.domain.schedule.service.strategy;

import com.yellobook.common.enums.ScheduleType;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO;
import com.yellobook.domain.schedule.dto.response.DailyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.UpcomingScheduleDTO;
import com.yellobook.domain.schedule.mapper.ScheduleMapper;
import com.yellobook.domains.schedule.dto.QueryMonthlyScheduleDTO;
import com.yellobook.domains.schedule.dto.QueryScheduleDTO;
import com.yellobook.domains.schedule.dto.QueryUpcomingScheduleDTO;
import com.yellobook.domains.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OrdererScheduleStrategy implements ScheduleStrategy{
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public UpcomingScheduleDTO getUpcomingSchedules(TeamMemberVO teamMember) {
        LocalDate today = LocalDate.now();
        QueryUpcomingScheduleDTO order = scheduleRepository.findEarliestOrder(today, teamMember).orElse(null);
        QueryUpcomingScheduleDTO inform = scheduleRepository.findEarliestInform(today, teamMember).orElse(null);
        QueryUpcomingScheduleDTO schedule = this.selectSchedule(order, inform);
        if(schedule != null) {
            return scheduleMapper.toUpcomingScheduleDTO(schedule);
        }
        return new UpcomingScheduleDTO("일정이 없습니다.");
    }


    @Override
    public SearchMonthlyScheduleDTO searchMonthlySchedules(MonthlySearchParam monthlySearchParam, TeamMemberVO teamMember) {
        String keyword = monthlySearchParam.getKeyword();
        int year = monthlySearchParam.getYear();
        int month = monthlySearchParam.getMonth();
        List<QueryScheduleDTO> orders = scheduleRepository.searchMonthlyOrders(keyword, year, month, teamMember);
        List<QueryScheduleDTO> informs = scheduleRepository.searchMonthlyInforms(keyword, year, month, teamMember);
        List<QueryScheduleDTO> schedules = new ArrayList<>();
        schedules.addAll(orders);
        schedules.addAll(informs);
        List<QueryScheduleDTO> sortedSchedules = schedules.stream().sorted(this::compareQueryScheduleDTO).toList();
        return new SearchMonthlyScheduleDTO(sortedSchedules);
    }


    @Override
    public CalendarScheduleDTO getCalendarSchedules(MonthlyParam monthlyParam, TeamMemberVO teamMember) {
        int year = monthlyParam.getYear();
        int month = monthlyParam.getMonth();
        List<QueryMonthlyScheduleDTO> orders = scheduleRepository.findMonthlyOrders(year, month, teamMember);
        List<QueryMonthlyScheduleDTO> informs = scheduleRepository.findMonthlyInforms(year, month, teamMember);

        Map<Integer, List<String>> calendarMap = new HashMap<>();
        this.addScheduleToCalendarMap(calendarMap, orders);
        this.addScheduleToCalendarMap(calendarMap, informs);
        return scheduleMapper.mapToCalendarScheduleDTO(calendarMap);
    }


    @Override
    public DailyScheduleDTO getDailySchedules(DailyParam dailyParam, TeamMemberVO teamMember) {
        int year = dailyParam.getYear();
        int month = dailyParam.getMonth();
        int day = dailyParam.getDay();
        List<QueryScheduleDTO>  orders = scheduleRepository.findDailyOrders(year,month,day,teamMember);
        List<QueryScheduleDTO> informs = scheduleRepository.findDailyInforms(year,month,day,teamMember);
        List<QueryScheduleDTO> schedules = new ArrayList<>();
        schedules.addAll(orders);
        schedules.addAll(informs);
        List<QueryScheduleDTO> sortedSchedules = schedules.stream().sorted(this::compareQueryScheduleDTO).toList();
        scheduleRepository.findDailyInforms(year,month,day,teamMember);
        return new DailyScheduleDTO(sortedSchedules);
    }
}
