package com.yellobook.domain.work.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.work.service.WorkCommandService;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work")
@Tag(name = "\uD83D\uDCBC 업무", description = "Work API")
public class WorkController {
    private final WorkCommandService workCommandService;

    @Operation(summary = "업무 삭제")
    @DeleteMapping("/{workId}")
    public ResponseEntity<SuccessResponse<String>> deleteWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        workCommandService.deleteWork(workId, oAuth2User);
        return null;
    }
}
