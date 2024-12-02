package com.yellobook.core.domains.team.entity;

import com.yellobook.core.domains.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "teams",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_team_name", columnNames = "name")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Builder
    public Team(String name, String phoneNumber, String address) {
        isValid(name, phoneNumber, address);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    private void isValid(String name, String phoneNumber, String address) {
        if (name == null || name.isEmpty() || phoneNumber == null || phoneNumber.isEmpty() || address == null
                || address.isEmpty()) {
            throw new IllegalArgumentException("빈칸이 존재합니다.");
        }
    }
}
