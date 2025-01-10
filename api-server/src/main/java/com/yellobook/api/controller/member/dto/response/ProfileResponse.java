package com.yellobook.api.controller.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record ProfileResponse(
        @Schema(description = "멤버의 고유 키 값", example = "123")
        Long memberId,

        @Schema(description = "멤버의 닉네임", example = "yellow")
        String nickname,

        @Schema(description = "멤버의 이메일 주소", example = "example@example.com")
        String email,

        @Schema(description = "프로필 이미지의 URL", example = "http://example.com/image.jpg")
        String profileImage,

        @Schema(description = "팀 이름 & 해당 팀에서 역할", example = "team1, 관리자")
        List<ParticipantInfo> teams
) {

    @Builder
    public record ParticipantInfo(
            @Schema(description = "팀 이름", example = "team1")
            String teamName,
            @Schema(description = "팀 ID", example = "1")
            Long teamId,
            @Schema(description = "팀에서 역할", example = "관리자")
            String role
    ) {
    }

}
