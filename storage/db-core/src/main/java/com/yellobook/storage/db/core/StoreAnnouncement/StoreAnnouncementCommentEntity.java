package com.yellobook.storage.db.core.StoreAnnouncement;

import com.yellobook.core.domain.StoreAnnouncement.StoreAnnouncementComment;
import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "store_announcement_comments")
public class StoreAnnouncementCommentEntity extends BaseEntity {
    @Column(length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private StoreAnnouncementEntity announcement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    protected StoreAnnouncementCommentEntity() {
    }

    public StoreAnnouncementCommentEntity(String content, StoreAnnouncementEntity announcement, MemberEntity member) {
        this.content = content;
        this.announcement = announcement;
        this.member = member;
    }

    public StoreAnnouncementComment toStoreAnnouncementComment() {
        return new StoreAnnouncementComment(
                this.getId(),
                content,
                member.toMember(),
                createdAt
        );
    }

    public String getContent() {
        return content;
    }

    public StoreAnnouncementEntity getAnnouncement() {
        return announcement;
    }

    public MemberEntity getMember() {
        return member;
    }
}
