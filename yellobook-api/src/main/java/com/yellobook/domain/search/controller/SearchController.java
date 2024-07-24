package com.yellobook.domain.search.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@Tag(name = "\uD83D\uDD0E 검색", description = "Search API")
public class SearchController {

    // 다가오는 주문, 공지, 업무 목록 검색
    @GetMapping("/schedule/upcoming")
    public ResponseEntity<?> getUpcomingSchedules() {
        return null;
    }

    // 해당 월의 날짜별 주문, 공지, 업무 검색 종합
    @GetMapping("/schedule/monthly")
    public ResponseEntity<?> getMonthSchedules(
    ) {
        return null;
    }

    // 해당 월의 키워드에 해당하는 주문, 공지, 업무 검색
    @GetMapping("/schedule")
    public ResponseEntity<?> searchSchedules() {
        return null;
    }

    // 오늘일자의 주문, 공지, 업무 검색
    @GetMapping("/schedule/today")
    public ResponseEntity<?> getTodaySchedules(
    ) {
        return null;
    }
}
