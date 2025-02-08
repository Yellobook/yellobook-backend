package com.yellobook.storage.db.core.team;

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

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    protected TeamEntity() {
    }

    public TeamEntity(String name, String description, String phoneNumber, String address) {
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
