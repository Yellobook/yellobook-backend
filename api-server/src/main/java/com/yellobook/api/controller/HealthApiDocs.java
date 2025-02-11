package com.yellobook.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "HEALTHCHECK API", description = "Health Check Endpoints")

public interface HealthApiDocs {

    @Operation(summary = "애플리케이션 헬스체크")
    String healthCheck();
}
