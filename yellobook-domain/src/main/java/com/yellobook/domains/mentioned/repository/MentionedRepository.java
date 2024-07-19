package com.yellobook.domains.mentioned.repository;

import com.yellobook.domains.mentioned.entity.Mentioned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentionedRepository extends JpaRepository<Mentioned, Long> {
}
