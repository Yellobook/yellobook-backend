package com.yellobook.domain.announce.controller;

import com.yellobook.domain.announce.dto.AnnounceRequest;
import com.yellobook.domain.announce.dto.AnnounceResponse;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.announce.repository.AnnounceRepository;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/announce")
@Tag(name = "\uD83D\uDCE2 공지", description = "Announce API")
public class AnnounceController {
    private final AnnounceRepository announceRepository;

    @PostMapping("/")
    @Operation(summary = "공지 작성 API", description = "공지를 생성하는 API 입니다.")
    public ResponseEntity<SuccessResponse<AnnounceResponse.PostAnnounceResponseDTO>> postAnnounce(
            @RequestBody AnnounceRequest.PostAnnounceRequestDTO request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @DeleteMapping("/{announceId}")
    @Operation(summary = "공지 삭제 API", description = "등록된 공지를 삭제하는 API 입니다.")
    public ResponseEntity<SuccessResponse<AnnounceResponse.RemoveAnnounceResponseDTO>> removeAnnounce(
            @PathVariable Long announceId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @GetMapping("/{announceId}")
    @Operation(summary = "공지 조회 API", description = "등록된 공지를 조회하는 API 입니다.")
    public ResponseEntity<SuccessResponse<AnnounceResponse.GetAnnounceResponseDTO>> getAnnounce(
            @PathVariable Long announceId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }
}
