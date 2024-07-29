package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.dto.MemberJoinTeamDTO;

import java.util.List;

public interface ParticipantCustomRepository {
    List<MemberJoinTeamDTO> getMemberJoinTeam(Long memberId);
}
