package com.yellobook.domain.inform.controller;

import com.yellobook.common.annotation.ExistInform;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inform.dto.InformCommentRequest;
import com.yellobook.domain.inform.dto.InformCommentResponse;
import com.yellobook.domain.inform.dto.InformRequest;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domain.inform.service.InformCommandService;
import com.yellobook.domain.inform.service.InformQueryService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/informs")
@Tag(name = "\uD83D\uDCBC 업무 및 공지", description = "Inform API")
public class InformController {
    private final InformQueryService informQueryService;
    private final InformCommandService informCommandService;

    @PostMapping("/")
    @Operation(summary = "업무 및 공지 작성 API", description = "업무 및 공지를 생성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<InformResponse.CreateInformResponseDTO>> createInform(
            @RequestBody InformRequest.CreateInformRequestDTO request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        InformResponse.CreateInformResponseDTO response = informCommandService.createInform(customOAuth2User, request);
        return ResponseFactory.created(response);
    }

    @Operation(summary = "업무(공지) 삭제")
    @DeleteMapping("/{informId}")
    public ResponseEntity<SuccessResponse<InformResponse.RemoveInformResponseDTO>> deleteInform(
            @ExistInform @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        InformResponse.RemoveInformResponseDTO response = informCommandService.deleteInform(informId, oAuth2User);
        return ResponseFactory.success(response);
    }

    @GetMapping("/{informId}")
    @Operation(summary = "업무(공지) 조회 API", description = "등록된 업무(공지)를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<InformResponse.GetInformResponseDTO>> getInform(
            @ExistInform @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        InformResponse.GetInformResponseDTO response = informQueryService.getInformById(customOAuth2User, informId);
        return ResponseFactory.success(response);
    }

    @PostMapping("/{informId}/comment")
    @Operation(summary = "댓글 작성 API", description = "댓글을 작성하는 API입니다.")
    public ResponseEntity<SuccessResponse<InformCommentResponse.PostCommentResponseDTO>> addComment(
            @ExistInform @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody InformCommentRequest.PostCommentRequestDTO request
    ){
        InformCommentResponse.PostCommentResponseDTO response = informCommandService.addComment(informId, customOAuth2User, request);
        return ResponseFactory.created(response);
    }
}
