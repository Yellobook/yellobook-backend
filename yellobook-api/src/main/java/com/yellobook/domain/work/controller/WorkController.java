package com.yellobook.domain.work.controller;

import com.yellobook.domain.work.service.WorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/works")
@Tag(name = "\uD83D\uDCBC 업무", description = "Work API")
public class WorkController {
    private final WorkService workService;
}
