package com.yellobook.domain.member.dto;

import com.yellobook.domains.team.dto.MemberJoinTeamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MemberResponse {
    @Getter
    public static class ProfileResponseDTO{

        @Schema(description = "멤버의 고유 키 값", example ="123")
        private Long memberId;

        @Schema(description = "멤버의 닉네임", example ="yellow")
        private String nickname;

        @Schema(description = "프로필 이미지의 URL", example ="http://example.com/image.jpg")
        private String profileImage;

        @Schema(description = "멤버의 이메일 주소", example ="example@example.com")
        private String email;

        @Schema(description = "멤버의 역할", example ="ADMIN")
        private List<ParticipantInfo> teams;

        @Builder
        public ProfileResponseDTO(Long memberId, String nickname, String profileImage, String email, List<ParticipantInfo> teams){
            this.memberId = memberId;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.email = email;
            this.teams = teams;
        }

    }

    @Getter
    public static class ParticipantInfo{
        private final String role;
        private final String teamName;

        @Builder
        public ParticipantInfo(String role, String teamName){
            this.role = role;
            this.teamName = teamName;
        }

        public ParticipantInfo(MemberJoinTeamDTO memberJoinTeamDTO){
            this.role = memberJoinTeamDTO.getRole().getDescription();
            this.teamName = memberJoinTeamDTO.getTeamName();
        }
    }

    @Getter
    public static class AllowanceResponseDTO {
        @Schema(description = "해당 멤버의 약관 동의 여부", example ="true")
        private final boolean allowance;

        @Builder
        public AllowanceResponseDTO(boolean allowance){
            this.allowance = allowance;
        }
    }
}

