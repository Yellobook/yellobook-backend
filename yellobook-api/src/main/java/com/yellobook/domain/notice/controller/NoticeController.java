package com.yellobook.domain.notice.controller;

import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notices/{teamId}")
@RequiredArgsConstructor
@Tag(name = " \uD83C\uDFE2 공지 & 업무" , description = "Announce & Work API")
public class NoticeController {

    @Operation(summary = "공지/업무 삭제")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse<String>> deleteNotice(
            @PathVariable("teamId") Long teamId,
            @PathVariable("noticeId") Long noticeId
    ){
        return null;
    }

}
