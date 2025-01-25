package com.yellobook.team;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.Team;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import java.util.List;

public interface TeamCustomRepository {
    List<QueryTeamMember> findTeamMembers(Long teamId);

    List<Team> getTeamsByMemberId(Long memberId);

    List<Member> getParticipantsByTeamId(Long teamId);

}
