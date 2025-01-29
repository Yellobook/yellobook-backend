package com.yellobook.team;

import com.yellobook.BaseEntity;
import com.yellobook.core.domain.team.Searchable;
import com.yellobook.core.domain.team.Team;
import jakarta.persistence.*;
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

    protected TeamEntity() {
    }

    @Builder
    private TeamEntity(String name, String phoneNumber, String address, Searchable searchable) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.searchable = searchable;
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
}
