package com.yellobook.common.controller;

import com.yellobook.common.anonotation.ApiV1Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@ApiV1Controller
@SecurityRequirement(name = "")
@Tag(name = "✅ 헬스체크", description = "Health Check Endpoint")
public class HealthController {

    @Operation(summary = "애플리케이션 헬스체크")
    @GetMapping({"/health", "/"})
    public String healthCheck() {
        log.info("Health Check 실행");
        return "Yellobook Server is healthy";
    }
}
