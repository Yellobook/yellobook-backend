package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository {
    Long save(NewAnnouncement newAnnouncement);

    Boolean isAnnouncementAuthor(Member member, Long announcementId);
}
