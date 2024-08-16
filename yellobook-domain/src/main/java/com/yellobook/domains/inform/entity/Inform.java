package com.yellobook.domains.inform.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "informs")
public class Inform extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 공지 및 업무 제목
     */
    private String title;

    /**
     * 공지 및 업무 내용
     */
    @Column(length = 200)
    private String content;

    /**
     * 조회수
     */
    @Column(nullable = false)
    private Integer view = 0;

    /**
     * 캘린더에 표시되어야 하는 날짜
     */
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany(mappedBy = "inform", cascade = CascadeType.REMOVE)
    private List<InformComment> comments = new ArrayList<>();

    @Builder
    private Inform(String title, String content, LocalDate date, Member member, Team team) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.member = member;
        this.team = team;
        this.view = 0;
    }

    public void updateView() {
        this.view += 1;
    }

    public void addComment(InformComment comment) {
        comments.add(comment);
        comment.setInform(this);
    }
}
