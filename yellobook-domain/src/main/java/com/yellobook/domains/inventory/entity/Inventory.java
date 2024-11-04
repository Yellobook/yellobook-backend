package com.yellobook.domains.inventory.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.team.entity.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inventories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 재고현황 사용 팀
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    /**
     * 재고 현황 제목 ex) 2024년 07월 24일 재고현황
     */
    @Column(nullable = false)
    private String title;

    /**
     * 조회수
     */
    @Column(nullable = false)
    private Integer view;

    @Builder
    private Inventory(Team team, String title) {
        this.team = team;
        this.title = title;
        this.view = 0;
    }

//    public void increaseView() {
//        this.view += 1;
//    }
}
