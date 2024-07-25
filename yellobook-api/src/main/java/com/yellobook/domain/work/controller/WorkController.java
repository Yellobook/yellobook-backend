package com.yellobook.domain.work.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.work.dto.WorkCommentRequest;
import com.yellobook.domain.work.dto.WorkCommentResponse;
import com.yellobook.domain.work.dto.WorkRequest;
import com.yellobook.domain.work.dto.WorkResponse;
import com.yellobook.domain.work.service.WorkCommandService;
import com.yellobook.domain.work.service.WorkQueryService;
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
    private final WorkQueryService workQueryService;
    private final WorkCommandService workCommandService;

    @PostMapping("/")
    @Operation(summary = "업무 작성 API", description = "업무를 생성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<WorkResponse.CreateWorkResponseDTO>> createWork(
            @RequestBody WorkRequest.CreateWorkRequestDTO workRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return null;
    }

    @Operation(summary = "업무 삭제")
    @DeleteMapping("/{workId}")
    public ResponseEntity<SuccessResponse<String>> deleteWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        workCommandService.deleteWork(workId, oAuth2User);
        return null;
    }

    @GetMapping("/{workId}")
    @Operation(summary = "업무 조회 API", description = "등록된 업무를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<WorkResponse.GetWorkResponseDTO>> getWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @PostMapping("/{workId}/comment")
    @Operation(summary = "댓글 작성 API", description = "댓글을 작성하는 API입니다.")
    public ResponseEntity<SuccessResponse<WorkCommentResponse.PostCommentResponseDTO>> createComment(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody WorkCommentRequest.PostCommentRequestDTO request
            ){
        return null;
    }

    @GetMapping("{workId}/comment")
    @Operation(summary = "댓글 가져오기 API", description = "작성된 댓글을 가져오는 API입니다.")
    public ResponseEntity<SuccessResponse<WorkCommentResponse.CommentResponseDTO>> getComment(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }
}
