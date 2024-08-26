package com.yellobook.domains.inform.controller;

import com.yellobook.common.validation.annotation.ExistInform;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.inform.dto.request.CreateInformCommentRequest;
import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.service.InformCommandService;
import com.yellobook.domains.inform.service.InformQueryService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/informs")
@Tag(name = "\uD83D\uDCBC 공지", description = "Inform API")
public class InformController {
    private final InformQueryService informQueryService;
    private final InformCommandService informCommandService;

    @PostMapping
    @Operation(summary = "공지 작성", description = "새로운 공지를 생성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<CreateInformResponse>> createInform(
            @RequestBody CreateInformRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        var result = informCommandService.createInform(customOAuth2User.getMemberId(), request);
        return ResponseFactory.created(result);
    }

    @DeleteMapping("/{informId}")
    @Operation(summary = "공지 삭제", description = "등록된 공지를 삭제하는 API 입니다.")
    public ResponseEntity<Void> deleteInform(
            @ExistInform @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        informCommandService.deleteInform(informId, oAuth2User.getMemberId());
        return ResponseFactory.noContent();
    }

    @GetMapping("/{informId}")
    @Operation(summary = "공지목록 조회", description = "등록된 공지를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<GetInformResponse>> getInform(
            @ExistInform @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        var result = informQueryService.getInformById(customOAuth2User.getMemberId(), informId);
        return ResponseFactory.success(result);
    }


    @PostMapping("/{informId}/comment")
    @Operation(summary = "공지 댓글 작성", description = "공지에 댓글을 작성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<CreateInformCommentResponse>> addComment(
            @ExistInform @PathVariable("informId") Long informId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody CreateInformCommentRequest request
    ){
        var result = informCommandService.addComment(informId, customOAuth2User.getMemberId(), request);
        return ResponseFactory.created(result);
    }
}
