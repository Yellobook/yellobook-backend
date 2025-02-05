package com.yellobook.storage.db.core.team;

import com.yellobook.core.domain.team.Team;
import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "teams",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_team_name", columnNames = "name")
        }
)
public class TeamEntity extends BaseEntity {
    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean isSearchable;

    protected TeamEntity() {
    }

    public TeamEntity(String name, String phoneNumber, String address, Boolean isSearchable) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isSearchable = isSearchable;
    }

    Team toTeam() {
        return new Team(
                id,
                name,
                phoneNumber,
                address,
                isSearchable
        );
    }
    
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

}
