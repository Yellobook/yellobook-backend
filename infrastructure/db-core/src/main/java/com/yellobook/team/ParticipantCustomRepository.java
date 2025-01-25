package com.yellobook.team;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.team.dto.query.QueryMemberJoinTeam;
import java.util.List;

public interface ParticipantCustomRepository {
    List<QueryMemberJoinTeam> getMemberJoinTeam(Long memberId);

    List<QueryTeamMember> findMentionsByNamePrefix(String prefix, Long teamId);

    List<Participant> getAllByTeamId(Long teamId);
}
