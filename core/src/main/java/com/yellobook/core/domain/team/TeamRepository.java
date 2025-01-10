package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository {
    List<Team> getTeamsByMemberId(Long memberId);

    boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role);  // participant jpa repo

    boolean existByTeamAndRole(Long teamId, TeamMemberRole role); // participant jpa repo
}
