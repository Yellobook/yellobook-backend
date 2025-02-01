package com.yellobook.storage.db.core.inform;

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
@Table(name = "informs")
public class InformEntity extends BaseEntity {
    private String title;

    @Column(length = 200)
    private String content;

    @Column(nullable = false)
    private Integer view = 0;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, insertable = false, updatable = false)
    private TeamEntity team;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "member_id")
    private Long memberId;

    protected InformEntity() {
    }

    public InformEntity(String title, String content, LocalDate date, Long teamId, Long memberId) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.memberId = memberId;
        this.teamId = teamId;
        this.view = 0;
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
