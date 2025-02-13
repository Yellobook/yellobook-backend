package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementCommentService {
    private final AnnouncementCommentWriter announcementCommentWriter;
    private final AnnouncementCommentAccessManager announcementCommentAccessManager;
    private final AnnouncementCommentReader announcementCommentReader;

    public AnnouncementCommentService(AnnouncementCommentWriter announcementCommentWriter,
                                      AnnouncementCommentAccessManager announcementCommentAccessManager,
                                      AnnouncementCommentReader announcementCommentReader) {
        this.announcementCommentWriter = announcementCommentWriter;
        this.announcementCommentAccessManager = announcementCommentAccessManager;
        this.announcementCommentReader = announcementCommentReader;
    }

    public Long createComment(NewAnnouncementComment newAnnounceComment) {
        announcementCommentAccessManager.isAbleToCreateComment(newAnnounceComment.member(),
                newAnnounceComment.announcementId());
        return announcementCommentWriter.createComment(newAnnounceComment);
    }

    public List<AnnouncementComment> getComments(Long announcementId) {
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
