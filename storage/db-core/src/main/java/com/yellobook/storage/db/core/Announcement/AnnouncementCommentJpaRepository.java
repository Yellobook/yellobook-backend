package com.yellobook.storage.db.core.Announcement;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementCommentJpaRepository extends JpaRepository<AnnouncementCommentEntity, Long> {
    List<AnnouncementCommentEntity> findByAnnouncementId(Long announcementId);

    void deleteByAnnouncementId(Long announcementId);
}
