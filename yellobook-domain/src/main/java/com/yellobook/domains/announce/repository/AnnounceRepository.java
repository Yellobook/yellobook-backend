package com.yellobook.domains.announce.repository;

import com.yellobook.domains.announce.entity.Announce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnounceRepository extends JpaRepository<Announce, Long> {
}
