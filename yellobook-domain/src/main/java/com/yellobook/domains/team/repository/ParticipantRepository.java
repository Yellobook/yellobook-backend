package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);
    Optional<Participant> findByMemberIdAndTeamId(Long memberId, Long teamId);
}
