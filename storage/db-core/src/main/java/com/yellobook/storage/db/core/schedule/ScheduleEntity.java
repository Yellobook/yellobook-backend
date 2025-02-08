package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.Schedule;
import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.team.TeamEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "schedule")
public class ScheduleEntity extends BaseEntity {
    private String title;

    @Column(length = 200)
    private String content;

    @Column(nullable = false)
    private Integer view = 0;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    protected ScheduleEntity() {
    }

    public ScheduleEntity(String title, String content, LocalDate date, MemberEntity member, TeamEntity team) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.view = 0;
        this.member = member;
        this.team = team;
    }

    Schedule toSchedule() {
        return new Schedule(
                this.getId(),
                member.toMember(),
                title,
                content,
                view,
                date
        );
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getView() {
        return view;
    }

    public LocalDate getDate() {
        return date;
    }

    public MemberEntity getMember() {
        return member;
    }

    public TeamEntity getTeam() {
        return team;
    }
}
