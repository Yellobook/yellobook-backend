package com.yellobook.domain.member.dto;

import com.yellobook.enums.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileResponseDTO{

        @Schema(description = "멤버의 고유 키 값", example ="123")
        private Long memberId;

        @Schema(description = "멤버의 닉네임", example ="yellow")
        private String nickname;

        @Schema(description = "프로필 이미지의 URL", example ="http://example.com/image.jpg")
        private String profileImage;

        @Schema(description = "멤버의 역할", example ="ADMIN")
        private MemberRole memberRole;

        @Schema(description = "멤버의 이메일 주소", example ="example@example.com")
        private String email;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllowanceResponseDTO {
        @Schema(description = "해당 멤버의 약관 동의 여부", example ="true")
        private boolean allowance;
    }
}

