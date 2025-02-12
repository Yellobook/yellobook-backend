package com.yellobook.core.domain.Announcement;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementCommentRepository {
    Long save(NewAnnouncementComment newAnnounceComment);

    List<AnnouncementComment> findByAnnouncementId(Long announcementId);
}
