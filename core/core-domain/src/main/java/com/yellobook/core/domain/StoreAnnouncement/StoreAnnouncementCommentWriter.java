package com.yellobook.core.domain.StoreAnnouncement;

import org.springframework.stereotype.Component;

@Component
public class StoreAnnouncementCommentWriter {
    private final StoreAnnouncementCommentRepository announcementCommentRepository;

    public StoreAnnouncementCommentWriter(StoreAnnouncementCommentRepository announcementCommentRepository) {
        this.announcementCommentRepository = announcementCommentRepository;
    }

    public Long createComment(NewStoreAnnouncementComment newAnnounceComment) {
        return announcementCommentRepository.save(newAnnounceComment);
    }

    public void deleteAllCommentsByAnnouncementId(Long announcementId) {
        announcementCommentRepository.deleteByAnnouncementId(announcementId);
    }

    public void deleteCommentById(Long commentId) {
        announcementCommentRepository.deleteByCommentId(commentId);
    }
}
