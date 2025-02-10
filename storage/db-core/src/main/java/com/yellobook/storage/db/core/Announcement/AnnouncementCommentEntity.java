package com.yellobook.storage.db.core.Announcement;

import com.yellobook.storage.db.core.BaseEntity;
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

    protected AnnouncementCommentEntity() {
    }

    public AnnouncementCommentEntity(String content, AnnouncementEntity announcement) {
        this.content = content;
        this.announcement = announcement;
    }

    public String getContent() {
        return content;
    }

    public AnnouncementEntity getAnnouncement() {
        return announcement;
    }
}
