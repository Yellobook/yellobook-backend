package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import com.yellobook.domains.team.entity.Participant;

import java.util.List;

public interface ParticipantCustomRepository {
    List<QueryMemberJoinTeam> getMemberJoinTeam(Long memberId);
    List<Participant> findMentionsByNamePrefix(String prefix, Long teamId);
}
