package com.yellobook.domain.announce.controller;

import com.yellobook.domain.announce.service.AnnounceCommandService;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
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
@RequestMapping("/api/v1/announce")
@Tag(name = "\uD83D\uDCE2 공지", description = "Announce API")
public class AnnounceController {
    private final AnnounceCommandService announceCommandService;

    @Operation(summary = "공지 삭제")
    @DeleteMapping("/{announceId}")
    public ResponseEntity<SuccessResponse<String>> deleteAnnounce(
            @PathVariable("announceId") Long announceId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        announceCommandService.deleteAnnounce(announceId, oAuth2User);
        return null;
    }

}
