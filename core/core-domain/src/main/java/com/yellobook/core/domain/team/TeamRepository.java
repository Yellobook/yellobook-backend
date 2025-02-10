package com.yellobook.core.domain.team;


import com.yellobook.core.enums.TeamMemberRole;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository {
    List<Team> getTeamsByMemberId(Long memberId);

    boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role);

    boolean existByTeamAndRole(Long teamId, TeamMemberRole role);

    Long save(String name, String description, String phoneNumber, String address, Boolean searchable);

    Optional<Team> findById(Long teamId);

    List<Participant> getParticipantsByTeamId(Long teamId);

    void join(Long teamId, Long memberId, TeamMemberRole role);

    void leave(Long teamId, Long memberId);

    boolean existByName(String name);

    List<Team> getSearchableTeamsByName(String keyword);

    void updateSearchable(Long teamId, Boolean searchable);

    boolean isTeamMember(Long teamId, Long memberId);

    void updateTeamMemberRole(Long teamId, Long memberId, TeamMemberRole role);

    int countAllByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole role);

    Optional<Participant> findParticipantByTeamIdAndMemberId(Long teamId, Long memberId);

}
