package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantRepositoryCustom {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);
    Optional<Participant> findByMemberIdAndTeamId(Long memberId, Long teamId);
    Optional<Participant> findByTeamId(Long teamId);
    List<Participant> findAllByTeamId(Long teamId);
}
