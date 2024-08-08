package com.yellobook.domains.team.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "teams",
    uniqueConstraints = {
        @UniqueConstraint(name = "uc_team_name", columnNames = "name")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Builder
    public Team(String name, String phoneNumber, String address) {
        isValid(name,phoneNumber,address);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    private void isValid(String name, String phoneNumber, String address){
        if(name == null || name.isEmpty() || phoneNumber == null || phoneNumber.isEmpty() || address == null || address.isEmpty()){
            throw new IllegalArgumentException("빈칸이 존재합니다.");
        }
    }
}
