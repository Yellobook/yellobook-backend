package com.yellobook.storage.db.core.schedule;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "schedule_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_schedule", columnNames = {"member_id", "schedule_id"})
        }
)
public class ScheduleMentionEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    protected ScheduleMentionEntity() {
    }

    public MemberEntity getMember() {
        return member;
    }

    public ScheduleEntity getSchedule() {
        return schedule;
    }
}