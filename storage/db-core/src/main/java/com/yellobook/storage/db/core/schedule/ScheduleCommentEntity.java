package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.ScheduleComment;
import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "schedule_comments")
public class ScheduleCommentEntity extends BaseEntity {
    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    protected ScheduleCommentEntity() {
    }

    public ScheduleCommentEntity(String content, MemberEntity member, ScheduleEntity schedule) {
        this.content = content;
        this.member = member;
        this.schedule = schedule;
    }

    ScheduleComment toScheduleComment() {
        return new ScheduleComment(
                this.getId(),
                member.toMember(),
                content,
                createdAt);
    }

    public String getContent() {
        return content;
    }

    public MemberEntity getMember() {
        return member;
    }

    public ScheduleEntity getSchedule() {
        return schedule;
    }
}
