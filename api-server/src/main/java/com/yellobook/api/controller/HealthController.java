package com.yellobook.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "")
public class HealthController implements HealthApiDocs {

    @GetMapping({"/health", "/"})
    public String healthCheck() {
        return "Yellobook Server is healthy";
    }
}
