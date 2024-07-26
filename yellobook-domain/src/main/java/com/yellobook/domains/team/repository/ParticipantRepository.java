package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.entity.Participant;
import com.yellobook.enums.MemberTeamRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);
    Optional<Participant> findByTeamIdAndMemberId(Long teamId, Long memberId);
    Optional<Participant> findByTeamIdAndRole(Long teamId, MemberTeamRole role);
    void deleteByTeamIdAndMemberId(Long teamId, Long memberId);
}
