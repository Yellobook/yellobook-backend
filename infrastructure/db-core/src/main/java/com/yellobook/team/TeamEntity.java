package com.yellobook.team;

import com.yellobook.BaseEntity;
import com.yellobook.core.domain.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    protected TeamEntity() {
    }

    @Builder
    private TeamEntity(String name, String phoneNumber, String address) {
        isValid(name, phoneNumber, address);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // 로직 분리 필요
    private void isValid(String name, String phoneNumber, String address) {
        if (name == null || name.isEmpty() || phoneNumber == null || phoneNumber.isEmpty() || address == null
                || address.isEmpty()) {
            throw new IllegalArgumentException("빈칸이 존재합니다.");
        }
    }

    Team toTeam() {
        return new Team(
                id,
                name,
                phoneNumber,
                address
        );
    }
}
