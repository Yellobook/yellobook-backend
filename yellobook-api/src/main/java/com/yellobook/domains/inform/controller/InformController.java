package com.yellobook.domains.inform.controller;

import com.yellobook.common.resolver.annotation.TeamMember;
import com.yellobook.common.validation.annotation.ExistInform;
import com.yellobook.common.vo.TeamMemberVO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @TeamMember TeamMemberVO teamMemberVO
    ) {
        var result = informCommandService.createInform(teamMemberVO.getMemberId(), request);
        return ResponseFactory.created(result);
    }

    @DeleteMapping("/{informId}")
    @Operation(summary = "공지 삭제", description = "등록된 공지를 삭제하는 API 입니다.")
    public ResponseEntity<Void> deleteInform(
            @ExistInform @PathVariable("informId") Long informId,
            @TeamMember TeamMemberVO teamMemberVO
    ){
        informCommandService.deleteInform(informId, teamMemberVO.getMemberId());
        return ResponseFactory.noContent();
    }

    @GetMapping("/{informId}")
    @Operation(summary = "공지 조회", description = "등록된 공지를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<GetInformResponse>> getInform(
            @ExistInform @PathVariable("informId") Long informId,
            @TeamMember TeamMemberVO teamMemberVO
    ){
        var result = informQueryService.getInformById(teamMemberVO.getMemberId(), informId);
        return ResponseFactory.success(result);
    }

    @PatchMapping("/{informId}/views")
    @Operation(summary = "조회수 증가", description = "조회하는 공지의 조회수 증가 API입니다.")
    public ResponseEntity<Void> increaseInformView(
            @ExistInform @PathVariable("informId") Long informId,
            @TeamMember TeamMemberVO teamMemberVO
    ) {
        informCommandService.increaseViewCount(informId, teamMemberVO);
        return ResponseFactory.noContent();
    }


    @PostMapping("/{informId}/comment")
    @Operation(summary = "공지 댓글 작성", description = "공지에 댓글을 작성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<CreateInformCommentResponse>> addComment(
            @ExistInform @PathVariable("informId") Long informId,
            @TeamMember TeamMemberVO teamMemberVO,
            @RequestBody CreateInformCommentRequest request
    ){
        var result = informCommandService.addComment(informId, teamMemberVO.getMemberId(), request);
        return ResponseFactory.created(result);
    }
}
