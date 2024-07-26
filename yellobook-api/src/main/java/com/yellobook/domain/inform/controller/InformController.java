package com.yellobook.domain.inform.controller;

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
    public ResponseEntity<SuccessResponse<String>> deleteInform(
            @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        informCommandService.deleteInform(informId, oAuth2User);
        return null;
    }

    @GetMapping("/{informId}")
    @Operation(summary = "업무(공지) 조회 API", description = "등록된 업무(공지)를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<InformResponse.GetInformResponseDTO>> getInform(
            @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @PostMapping("/{informId}/comment")
    @Operation(summary = "댓글 작성 API", description = "댓글을 작성하는 API입니다.")
    public ResponseEntity<SuccessResponse<InformCommentResponse.PostCommentResponseDTO>> addComment(
            @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody InformCommentRequest.PostCommentRequestDTO request
    ){
        return null;
    }

    @GetMapping("{informId}/comment")
    @Operation(summary = "댓글 가져오기 API", description = "작성된 댓글을 가져오는 API입니다.")
    public ResponseEntity<SuccessResponse<InformCommentResponse.CommentResponseDTO>> getComment(
            @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }
}
