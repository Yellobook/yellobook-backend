package com.yellobook.domains.schedule.controller;

import com.yellobook.common.resolver.annotation.TeamMember;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.schedule.ScheduleService;
import com.yellobook.domains.schedule.dto.request.DailyParam;
import com.yellobook.domains.schedule.dto.request.MonthlyParam;
import com.yellobook.domains.schedule.dto.request.MonthlySearchParam;
import com.yellobook.domains.schedule.dto.response.CalendarResponse;
import com.yellobook.domains.schedule.dto.response.DailyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.SearchMonthlyScheduleResponse;
import com.yellobook.domains.schedule.dto.response.UpcomingScheduleResponse;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
@Tag(name = "\uD83D\uDCC5 일정", description = "Schedule API")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "다가오는 일정")
    @GetMapping("/upcoming")
    public ResponseEntity<SuccessResponse<UpcomingScheduleResponse>> getUpcomingSchedules(
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.getUpcomingSchedules(teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "월별 키워드에 해당하는 일정")
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SearchMonthlyScheduleResponse>> searchMonthlySchedules(
            @Valid @ModelAttribute MonthlySearchParam params,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.searchMonthlySchedules(params, teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "캘린더 월별 종합 일정")
    @GetMapping("/monthly")
    public ResponseEntity<SuccessResponse<CalendarResponse>> getCalendarSchedules(
            @Valid @ModelAttribute MonthlyParam params,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.getCalendarSchedules(params, teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "날짜별 일정")
    @GetMapping("/daily")
    public ResponseEntity<SuccessResponse<DailyScheduleResponse>> getDailySchedules(
            @Valid @ModelAttribute DailyParam params,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = scheduleService.getDailySchedules(params, teamMember);
        return ResponseFactory.success(result);
    }
}
