package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository {
    List<Team> getTeamsByMemberId(Long memberId);

    boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role);

    boolean existByTeamAndRole(Long teamId, TeamMemberRole role);

    boolean existByTeamIdAndMemberId(Long teamId, Long memberId);

    Long save(String name, String phoneNumber, String address, Searchable searchable);

    Optional<Team> findById(Long teamId);

    List<Participant> getMembersByTeamId(Long teamId);

    void join(Long teamId, Long memberId, TeamMemberRole role);

    void leave(Long teamId, Long memberId);

    boolean existByName(String name);

    TeamMemberRole getRole(Long teamId, Long memberId);

    List<Team> getPublicTeamsByName(String keyword);

    void updateSearchable(Long teamId, Searchable searchable);

    boolean isTeamMember(Long teamId, Long memberId);

    void updateTeamMemberRole(Long teamId, Long memberId, TeamMemberRole role);

    void deactivateTeam(Long teamId);
}
