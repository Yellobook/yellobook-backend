package com.yellobook.domains.team.repository;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;

import java.util.List;

public interface ParticipantRepositoryCustom {
    List<Participant> findMentionsByNamePrefix(String prefix, Long teamId);
}
