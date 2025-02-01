//package com.yellobook.storage.db.core.team;
//
//import com.yellobook.core.domain.common.TeamMemberRole;
//import java.util.Optional;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//
//public interface ParticipantJpaRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
//    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);
//
//    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);
//
//    void deleteByTeamIdAndMemberId(Long teamId, Long memberId);
//
//    Optional<Participant> findByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole teamMemberRole);
//
//    boolean existsByTeamEntityIdAndMemberEntityIdAndTeamMemberRole(Long teamId, Long memberId,
//                                                                   TeamMemberRole teamMemberRole);
//
//    boolean existsByTeamEntityIdAndTeamMemberRole(Long teamId, TeamMemberRole role);
//
//    Participant findByTeamIdAndMemberId(Long teamId, Long memberId);
//}
