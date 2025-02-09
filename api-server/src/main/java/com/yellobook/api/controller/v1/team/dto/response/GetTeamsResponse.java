package com.yellobook.api.controller.v1.team.dto.response;

import com.yellobook.core.domain.team.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record GetTeamsResponse(
        List<TeamResponse> teams

) {
    public record TeamResponse(
            @Schema(description = "팀의 고유 id", example = "123")
            Long teamId,
            @Schema(description = "팀의 이름", example = "나이키")
            String name,
            @Schema(description = "팀 설명", example = "00이네 딸기농장 입니다.")
            String description,
            @Schema(description = "팀의 전화번호", example = "012345678")
            String phoneNumber,
            @Schema(description = "팀의 주소", example = "서울특별시 강남구")
            String address
    ) {
    }

    public static GetTeamsResponse from(List<Team> teams) {
        return new GetTeamsResponse(
                teams.stream()
                        .map(t -> new TeamResponse(t.teamId(), t.name(), t.description(), t.phoneNumber(), t.address()))
                        .toList()
        );
    }

}
