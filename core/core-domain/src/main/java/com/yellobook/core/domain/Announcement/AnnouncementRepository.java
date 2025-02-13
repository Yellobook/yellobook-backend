package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository {
    Long save(NewAnnouncement newAnnouncement);

    Boolean isAnnouncementAuthor(Member member, Long announcementId);

    Optional<Announcement> getAnnouncementById(Long announcementId);

    void increaseView(Long announcementId);

    void deactivateAnnouncementPin(Long announcementId);

    void activateAnnouncementPin(Long announcementId);

    Boolean isPinExist(Long teamId);
}
