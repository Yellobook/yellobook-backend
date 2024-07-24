package com.yellobook.domains.announce.repository;

import com.yellobook.domains.announce.entity.AnnounceComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnounceCommentRepository extends JpaRepository<AnnounceComment, Long> {
}
