package com.yellobook.core.domain.Announcement;

import org.springframework.stereotype.Component;

@Component
public class AnnouncementCommentWriter {
    private final AnnouncementCommentRepository announcementCommentRepository;

    public AnnouncementCommentWriter(AnnouncementCommentRepository announcementCommentRepository) {
        this.announcementCommentRepository = announcementCommentRepository;
    }

    public Long createComment(NewAnnouncementComment newAnnounceComment) {
        return announcementCommentRepository.save(newAnnounceComment);
    }

    public void deleteAllCommentsByAnnouncementId(Long announcementId) {
        announcementCommentRepository.deleteByAnnouncementId(announcementId);
    }

    public void deleteCommentById(Long commentId) {
        announcementCommentRepository.deleteByCommentId(commentId);
    }
}
