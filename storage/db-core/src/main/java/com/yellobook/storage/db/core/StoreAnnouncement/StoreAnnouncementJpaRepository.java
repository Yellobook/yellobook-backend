package com.yellobook.storage.db.core.StoreAnnouncement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreAnnouncementJpaRepository extends JpaRepository<StoreAnnouncementEntity, Long> {
    @Modifying
    @Query(value = "update StoreAnnouncementEntity a set a.view = a.view +1 where a.id = :announcementId")
    void increaseView(@Param("announcementId") Long announcementId);

    @Modifying
    @Query(value = "update StoreAnnouncementEntity a set a.status = 'INACTIVE' where a.id = :announcementId")
    void deactivate(@Param("announcementId") Long announcementId);

    @Modifying
    @Query(value = "update StoreAnnouncementEntity a set a.status = 'ACTIVE' where a.id = :announcementId")
    void activate(@Param("announcementId") Long announcementId);
}
