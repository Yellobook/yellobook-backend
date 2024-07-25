package com.yellobook.domain.schedule.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.schedule.dto.request.DailyScheduleParam;
import com.yellobook.domain.schedule.dto.request.MonthlyScheduleParam;
import com.yellobook.domain.schedule.dto.request.SearchScheduleParam;
import com.yellobook.domain.schedule.dto.response.DailyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.MonthlyTotalScheduleDTO;
import com.yellobook.domain.schedule.dto.response.SearchMonthlyScheduleDTO;
import com.yellobook.domain.schedule.dto.response.UpcomingScheduleDTO;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
@Tag(name = "\uD83D\uDCC5 일정", description = "Schedule API")
public class ScheduleController {
    // 다가오는 최근 일정 조회
    @Operation(summary = "다가오는 일정")
    @GetMapping("/teams/{teamId}/upcoming")
    public ResponseEntity<SuccessResponse<UpcomingScheduleDTO>> getUpcomingSchedules(
            @PathVariable("teamId") String teamId,
            @AuthenticationPrincipal CustomOAuth2User principal
    ) {
        return null;
    }

    //
    @Operation(summary = "월별 키워드에 해당하는 일정")
    @GetMapping("/teams/{teamId}/search")
    public ResponseEntity<SuccessResponse<SearchMonthlyScheduleDTO>> searchMonthlySchedules(
            @PathVariable("teamId") String teamId,
            @ModelAttribute SearchScheduleParam params,
            @AuthenticationPrincipal CustomOAuth2User user
            ) {
        return null;
    }

    @Operation(summary = "월별 종합 일정")
    @GetMapping("/teams/{teamId}/schedule/monthly")
    public ResponseEntity<SuccessResponse<MonthlyTotalScheduleDTO>> getMonthlyTotalSchedules(
            @PathVariable("teamId") String teamId,
            @ModelAttribute MonthlyScheduleParam params,
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        return null;
    }

    @Operation(summary = "날짜별 일정")
    @GetMapping("/teams/{teamId}/schedule/daily")
    public ResponseEntity<SuccessResponse<DailyScheduleDTO>> getDailySchedules(

            @PathVariable("teamId") String teamId,
            @ModelAttribute DailyScheduleParam params,
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        return null;
    }
}
