package com.yellobook.api.controller.member.dto.response;

import com.yellobook.core.domain.member.JoinedTeamResult;
import com.yellobook.core.domain.member.Member;
import java.util.List;

public record JoinedTeamsResponse(
        Long memberId,
        List<JoinedTeam> joinedTeams
) {
    public record JoinedTeam(
            Long teamId,
            String teamName,
            String teamDescription,
            String myRole,
            String sellerName,
            int memberCount
    ) {
    }

    public static JoinedTeamsResponse of(Member member, List<JoinedTeamResult> joinedTeams) {
        return new JoinedTeamsResponse(
                member.memberId(),
                joinedTeams.stream()
                        .map(team -> new JoinedTeam(
                                team.teamId(),
                                team.teamName(),
                                team.teamDescription(),
                                team.myRole(),
                                team.sellerName(),
                                team.memberCount()
                        ))
                        .toList()
        );
    }
}