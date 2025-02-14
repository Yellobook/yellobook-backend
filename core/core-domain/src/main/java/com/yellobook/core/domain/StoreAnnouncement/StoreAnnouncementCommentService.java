package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StoreAnnouncementCommentService {
    private final StoreAnnouncementCommentWriter announcementCommentWriter;
    private final StoreAnnouncementCommentAccessManager announcementCommentAccessManager;
    private final StoreAnnouncementCommentReader announcementCommentReader;

    public StoreAnnouncementCommentService(StoreAnnouncementCommentWriter announcementCommentWriter,
                                           StoreAnnouncementCommentAccessManager announcementCommentAccessManager,
                                           StoreAnnouncementCommentReader announcementCommentReader) {
        this.announcementCommentWriter = announcementCommentWriter;
        this.announcementCommentAccessManager = announcementCommentAccessManager;
        this.announcementCommentReader = announcementCommentReader;
    }

    public Long createComment(NewStoreAnnouncementComment newAnnounceComment) {
        announcementCommentAccessManager.isAbleToCreateComment(newAnnounceComment.member(),
                newAnnounceComment.announcementId());
        return announcementCommentWriter.createComment(newAnnounceComment);
    }

    public List<StoreAnnouncementComment> getComments(Long announcementId) {
        return announcementCommentReader.getComments(announcementId);
    }

    public void deleteAllComments(Long announcementId) {
        announcementCommentWriter.deleteAllCommentsByAnnouncementId(announcementId);
    }

    public void deleteComment(Member member, Long commentId) {
        announcementCommentAccessManager.isCommentAuthor(member, commentId);
        announcementCommentWriter.deleteCommentById(commentId);
    }
}
