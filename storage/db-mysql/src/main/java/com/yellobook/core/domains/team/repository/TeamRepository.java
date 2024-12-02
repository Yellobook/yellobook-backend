package com.yellobook.core.domains.team.repository;

import com.yellobook.core.domains.team.dto.query.QueryTeamMember;
import com.yellobook.core.domains.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
    Optional<Team> findByName(String name);

    List<QueryTeamMember> findTeamMembers(Long teamId);
}
