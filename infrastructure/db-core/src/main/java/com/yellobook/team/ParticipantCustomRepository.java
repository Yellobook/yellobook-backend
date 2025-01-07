package com.yellobook.team;

import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import java.util.List;

public interface ParticipantCustomRepository {
    List<QueryMemberJoinTeam> getMemberJoinTeam(Long memberId);

    List<QueryTeamMember> findMentionsByNamePrefix(String prefix, Long teamId);
}
