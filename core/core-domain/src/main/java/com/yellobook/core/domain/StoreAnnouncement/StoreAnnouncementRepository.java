package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreAnnouncementRepository {
    Long save(NewStoreAnnouncement newAnnouncement);

    Boolean isAnnouncementAuthor(Member member, Long announcementId);

    Optional<StoreAnnouncement> getAnnouncementById(Long announcementId);

    void increaseView(Long announcementId);

    void deactivateAnnouncementPin(Long announcementId);

    void activateAnnouncementPin(Long announcementId);

    Boolean isPinExist(Long teamId);

    void delete(Long announcementId);
}
