package com.yellobook.common.controller;

import com.yellobook.common.anonotation.ApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@ApiV1Controller
public class HealthController {
    @GetMapping({"/health", "/"})
    public String healthCheck() {
        log.info("Health Check 실행");
        return "Yellobook Server is healthy";
    }
}
