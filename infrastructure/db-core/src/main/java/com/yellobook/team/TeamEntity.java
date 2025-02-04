package com.yellobook.team;

import com.yellobook.BaseEntity;
import com.yellobook.core.domain.team.Searchable;
import com.yellobook.core.domain.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "teams",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_team_name", columnNames = "name")
        }
)
public class TeamEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private Searchable searchable;

    @Column(nullable = false)
    private boolean isDeleted;

    protected TeamEntity() {
    }

    @Builder
    private TeamEntity(String name, String phoneNumber, String address, Searchable searchable) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.searchable = searchable;
        this.isDeleted = false;
    }

    Team toTeam() {
        return new Team(
                id,
                name,
                phoneNumber,
                address,
                searchable
        );
    }

    void delete() {
        this.isDeleted = true;
    }
}
