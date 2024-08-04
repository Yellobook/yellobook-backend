package com.yellobook.domain.schedule.controller;

import com.yellobook.common.annotation.TeamMember;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.schedule.dto.request.DailyParam;
import com.yellobook.domain.schedule.dto.request.MonthlyParam;
import com.yellobook.domain.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domain.schedule.dto.response.DailyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.CalendarScheduleDTO;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.UpcomingScheduleDTO;
import com.yellobook.domain.schedule.service.ScheduleService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
@Tag(name = "\uD83D\uDCC5 일정", description = "Schedule API")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "다가오는 일정")
    @GetMapping("/upcoming")
    public ResponseEntity<SuccessResponse<UpcomingScheduleDTO>> getUpcomingSchedules(
            @TeamMember TeamMemberVO teamMember
            ) {
        var result = scheduleService.getUpcomingSchedules(teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "월별 키워드에 해당하는 일정")
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SearchMonthlyScheduleDTO>> searchMonthlySchedules(
            @ModelAttribute MonthlySearchParam params,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.searchMonthlySchedules(params, teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "캘린더 월별 종합 일정")
    @GetMapping("/monthly")
    public ResponseEntity<SuccessResponse<CalendarScheduleDTO>> getCalendarSchedules(
            @ModelAttribute MonthlyParam params,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.getCalendarSchedules(params, teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "날짜별 일정")
    @GetMapping("/daily")
    public ResponseEntity<SuccessResponse<DailyScheduleDTO>> getDailySchedules(
            @ModelAttribute DailyParam params,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.getDailySchedules(params, teamMember);
        return ResponseFactory.success(result);
    }
}
