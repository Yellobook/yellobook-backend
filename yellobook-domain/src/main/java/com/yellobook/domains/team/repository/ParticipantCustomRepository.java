package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.dto.MemberJoinTeamDTO;
import com.yellobook.domains.team.entity.Participant;

import java.util.List;

public interface ParticipantCustomRepository {
    List<MemberJoinTeamDTO> getMemberJoinTeam(Long memberId);
    List<Participant> findMentionsByNamePrefix(String prefix, Long teamId);
}
