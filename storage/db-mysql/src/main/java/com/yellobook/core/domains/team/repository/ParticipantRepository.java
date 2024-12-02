package com.yellobook.core.domains.team.repository;

import com.yellobook.core.core.enums.TeamMemberRole;
import com.yellobook.core.domains.team.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);

    List<Participant> findAllByTeamId(Long teamId);

    Optional<Participant> findByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<Participant> findByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole teamMemberRole);
}
