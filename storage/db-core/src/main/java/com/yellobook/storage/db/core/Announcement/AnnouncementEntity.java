package com.yellobook.storage.db.core.Announcement;

import com.yellobook.core.domain.Announcement.Announcement;
import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.team.TeamEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "announcements")
public class AnnouncementEntity extends BaseEntity {
    private String title;

    @Column(length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false)
    private int view;


    protected AnnouncementEntity() {
    }

    public AnnouncementEntity(String title, String content, MemberEntity member, TeamEntity team) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.team = team;
        this.view = 0;
    }

    Announcement toAnnouncement() {
        return new Announcement(
                this.getId(),
                title,
                content,
                member.toMember()
        );
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public MemberEntity getMember() {
        return member;
    }

    public TeamEntity getTeam() {
        return team;
    }
}
