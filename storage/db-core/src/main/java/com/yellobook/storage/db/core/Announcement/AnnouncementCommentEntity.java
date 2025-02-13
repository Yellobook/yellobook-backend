package com.yellobook.storage.db.core.Announcement;

import com.yellobook.core.domain.Announcement.AnnouncementComment;
import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "announcement_comments")
public class AnnouncementCommentEntity extends BaseEntity {
    @Column(length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private AnnouncementEntity announcement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    protected AnnouncementCommentEntity() {
    }

    public AnnouncementCommentEntity(String content, AnnouncementEntity announcement, MemberEntity member) {
        this.content = content;
        this.announcement = announcement;
        this.member = member;
    }

    public AnnouncementComment toAnnouncementComment() {
        return new AnnouncementComment(
                this.getId(),
                content,
                member.toMember(),
                createdAt
        );
    }

    public String getContent() {
        return content;
    }

    public AnnouncementEntity getAnnouncement() {
        return announcement;
    }

    public MemberEntity getMember() {
        return member;
    }
}
