package com.yellobook.core.domain.StoreAnnouncement;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StoreAnnouncementCommentReader {
    private final StoreAnnouncementCommentRepository announcementCommentRepository;

    public StoreAnnouncementCommentReader(StoreAnnouncementCommentRepository announcementCommentRepository) {
        this.announcementCommentRepository = announcementCommentRepository;
    }

    public List<StoreAnnouncementComment> getComments(Long announcementId) {
        return announcementCommentRepository.findByAnnouncementId(announcementId);
    }
}
