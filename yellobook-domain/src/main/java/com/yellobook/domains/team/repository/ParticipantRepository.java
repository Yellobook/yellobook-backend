package com.yellobook.domains.team.repository;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domains.team.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);
    List<Participant> findAllByTeamId(Long teamId);
    Optional<Participant> findByTeamIdAndMemberId(Long teamId, Long memberId);
    Optional<Participant> findByTeamIdAndRole(Long teamId, MemberTeamRole role);
}
