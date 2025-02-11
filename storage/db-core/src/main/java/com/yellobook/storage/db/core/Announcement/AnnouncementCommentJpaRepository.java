package com.yellobook.storage.db.core.Announcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementCommentJpaRepository extends JpaRepository<AnnouncementCommentEntity, Long> {
}
