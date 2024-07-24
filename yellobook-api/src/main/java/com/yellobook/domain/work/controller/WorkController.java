package com.yellobook.domain.work.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.work.dto.WorkRequest;
import com.yellobook.domain.work.dto.WorkResponse;
import com.yellobook.domain.work.service.WorkQueryServiceImpl;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/works")
@Tag(name = "\uD83D\uDCBC 업무", description = "Work API")
public class WorkController {
    private final WorkQueryServiceImpl workQueryService;

    @PostMapping("/{teamId}")
    @Operation(summary = "업무 작성 API", description = "업무를 생성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<WorkResponse.CreateWorkResponseDTO>> createWork(
            @PathVariable Long teamId,
            @RequestBody WorkRequest.CreateWorkRequestDTO workRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return null;
    }

    @DeleteMapping("/{teamId}/{workId}")
    @Operation(summary = "업무 삭제 API", description = "등록된 업무를 삭제하는 API 입니다.")
    public ResponseEntity<SuccessResponse<WorkResponse.RemoveWorkResponseDTO>> removeWork(
            @PathVariable Long teamId,
            @PathVariable Long workId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @GetMapping("/{teamId}/{workId}")
    @Operation(summary = "업무 조회 API", description = "등록된 업무를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<WorkResponse.GetWorkResponseDTO>> getWork(
            @PathVariable Long teamId,
            @PathVariable Long workId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }
}
