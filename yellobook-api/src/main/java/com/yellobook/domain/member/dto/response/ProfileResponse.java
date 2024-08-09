package com.yellobook.domain.member.dto.response;

import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileResponse(
        @Schema(description = "멤버의 고유 키 값", example ="123")
        Long memberId,

        @Schema(description = "멤버의 닉네임", example ="yellow")
        String nickname,

        @Schema(description = "프로필 이미지의 URL", example ="http://example.com/image.jpg")
        String profileImage,

        @Schema(description = "멤버의 이메일 주소", example ="example@example.com")
        String email,

        @Schema(description = "팀 이름 & 해당 팀에서 역할", example ="team1, 관리자")
        List<ParticipantInfo> teams
){

    public record ParticipantInfo(
            @Schema(description = "팀 이름", example ="team1")
            String teamName,
            @Schema(description = "팀에서 역할", example ="관리자")
            String role
    ){

        @Builder
        public static ParticipantInfo of(QueryMemberJoinTeam queryMemberJoinTeam){
            return new ParticipantInfo(
                    queryMemberJoinTeam.teamName(),
                    queryMemberJoinTeam.role().getDescription()
            );
        }
    }

}
