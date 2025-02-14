package com.yellobook.storage.db.core.StoreAnnouncement;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreAnnouncementCommentJpaRepository extends JpaRepository<StoreAnnouncementCommentEntity, Long> {
    List<StoreAnnouncementCommentEntity> findByAnnouncementId(Long announcementId);

    void deleteByAnnouncementId(Long announcementId);
}
