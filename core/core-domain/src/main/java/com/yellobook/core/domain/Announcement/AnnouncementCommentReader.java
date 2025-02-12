package com.yellobook.core.domain.Announcement;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementCommentReader {
    private final AnnouncementCommentRepository announcementCommentRepository;

    public AnnouncementCommentReader(AnnouncementCommentRepository announcementCommentRepository) {
        this.announcementCommentRepository = announcementCommentRepository;
    }

    public List<AnnouncementComment> getComments(Long announcementId) {
        return announcementCommentRepository.findByAnnouncementId(announcementId);
    }
}
