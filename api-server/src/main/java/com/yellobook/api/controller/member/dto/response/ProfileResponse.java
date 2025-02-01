package com.yellobook.api.controller.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yellobook.api.support.ApiMember;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ProfileResponse(
        @Schema(description = "고유 키 값", example = "123")
        Long memberId,

        @Schema(description = "이메일 주소", example = "opellong13@example.com")
        String email,

        @Schema(description = "닉네임", example = "우상스")
        String nickname,

        @Schema(description = "닉네임 마지막 변경일", example = "2024-01-31 15:45:30")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime nicknameUpdatedAt,

        @Schema(description = "자기소개란", example = "옐로북 개발자")
        String bio,

        @Schema(description = "프로필 이미지 URL", example = "http://example.com/image.jpg")
        String profileImage,

        @Schema(description = "소셜 연동 정보", example = "naver")
        String socialProvider
) {
    public static ProfileResponse of(ApiMember apiMember) {
        return new ProfileResponse(
                apiMember.memberId(),
                apiMember.socialInfo()
                        .email(),
                apiMember.profileInfo()
                        .nickname(),
                apiMember.profileInfo()
                        .nicknameUpdatedAt(),
                apiMember.profileInfo()
                        .bio(),
                apiMember.profileInfo()
                        .profileImage(),
                apiMember.socialInfo()
                        .provider()
        );
    }
}
