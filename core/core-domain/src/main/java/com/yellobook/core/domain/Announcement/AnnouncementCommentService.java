package com.yellobook.core.domain.Announcement;

import org.springframework.stereotype.Service;

@Service
public class AnnouncementCommentService {

    private final AnnouncementAccessManager announcementAccessManager;
    private final AnnouncementCommentWriter announcementCommentWriter;
    private final AnnouncementCommentAccessManager announcementCommentAccessManager;

    public AnnouncementCommentService(AnnouncementAccessManager announcementAccessManager,
                                      AnnouncementCommentWriter announcementCommentWriter,
                                      AnnouncementCommentAccessManager announcementCommentAccessManager) {
        this.announcementAccessManager = announcementAccessManager;
        this.announcementCommentWriter = announcementCommentWriter;
        this.announcementCommentAccessManager = announcementCommentAccessManager;
    }

    public Long createComment(NewAnnouncementComment newAnnounceComment) {
        announcementCommentAccessManager.isAbleToCreateComment(newAnnounceComment.member(),
                newAnnounceComment.announcementId());
        return announcementCommentWriter.createComment(newAnnounceComment);
    }
}
