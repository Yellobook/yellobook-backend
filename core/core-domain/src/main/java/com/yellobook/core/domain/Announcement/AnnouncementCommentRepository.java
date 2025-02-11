package com.yellobook.core.domain.Announcement;

import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementCommentRepository {
    Long save(NewAnnouncementComment newAnnounceComment);
}
