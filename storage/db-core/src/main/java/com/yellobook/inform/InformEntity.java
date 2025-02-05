package com.yellobook.inform;

import com.yellobook.BaseEntity;
import com.yellobook.member.MemberEntity;
import com.yellobook.team.TeamEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "informs")
public class InformEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    protected InformEntity() {
    }

//    @OneToMany(mappedBy = "inform", cascade = CascadeType.REMOVE)
//    private List<InformComment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "inform", cascade = CascadeType.REMOVE)
//    private List<InformMention> mentions = new ArrayList<>();

    @Builder
    private InformEntity(String title, String content, LocalDate date, MemberEntity member, TeamEntity team) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.member = member;
        this.team = team;
        this.view = 0;
    }

//    public void updateView() {
//        this.view += 1;
//    }
//
//    public void addComment(InformComment comment) {
//        comments.add(comment);
//        comment.setInform(this);
//    }
}
