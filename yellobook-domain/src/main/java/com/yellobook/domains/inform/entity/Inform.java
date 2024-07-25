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
    private Integer view;

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

    @Builder
    private Inform(String title, Integer view, String content, LocalDate date, Member member, Team team) {
        // 필수 필드 검증 추가할 것
        // 양방향 연관관계 필요하면 private 으로 빼서만들고 생성자에 추가할것
        this.title = title;
        this.view = view;
        this.content = content;
        this.date = date;
        this.member = member;
        this.team = team;
    }
}
