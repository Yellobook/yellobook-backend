package com.yellobook.team;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
    Optional<Team> findByName(String name);

    List<QueryTeamMember> findTeamMembers(Long teamId);
}