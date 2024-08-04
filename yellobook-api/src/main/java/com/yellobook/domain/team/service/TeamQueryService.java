package com.yellobook.domain.team.service;


import com.yellobook.domain.inform.dto.MentionDTO;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;

import java.util.List;

public interface TeamQueryService {
    Team findByTeamId(Long teamId, Long memberId);
    List<MentionDTO> searchParticipants(Long teamId, String name);
}
