package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
    Optional<Team> findByName(String name);

    List<QueryTeamMember> findTeamMembers(Long teamId);
}
