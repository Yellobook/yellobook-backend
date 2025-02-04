package com.yellobook.domains.team.repository;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.team.entity.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);

    List<Participant> findAllByTeamId(Long teamId);

    Optional<Participant> findByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<Participant> findByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole teamMemberRole);
}
