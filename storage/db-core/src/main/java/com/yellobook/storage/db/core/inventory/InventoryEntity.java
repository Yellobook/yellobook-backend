package com.yellobook.storage.db.core.inventory;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.team.TeamEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventories")
public class InventoryEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer view;

    protected InventoryEntity() {
    }

    public InventoryEntity(TeamEntity team, String title, Integer view) {
        this.team = team;
        this.title = title;
        this.view = view;
    }


    public TeamEntity getTeam() {
        return team;
    }

    public String getTitle() {
        return title;
    }

    public Integer getView() {
        return view;
    }
}
