package com.yellobook.domains.teamspace.repository;

import com.yellobook.domains.teamspace.entity.Teamspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamspaceRepository extends JpaRepository<Teamspace, Long> {
}
