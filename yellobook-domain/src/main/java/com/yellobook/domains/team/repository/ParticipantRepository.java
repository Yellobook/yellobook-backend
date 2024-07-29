package com.yellobook.domains.team.repository;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domains.team.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);
    Optional<Participant> findByTeamIdAndMemberId(Long teamId, Long memberId);
    Optional<Participant> findByTeamIdAndRole(Long teamId, MemberTeamRole role);
}
