package com.yellobook.domains.member.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "members",
        indexes = {
                @Index(name = "ix_members_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_members_email", columnNames = "email")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String email;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberRole role;

    @Column(nullable = false)
    private Boolean allowance;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    public Member(Long memberId, String nickname, String email, String profileImage, MemberRole role) {
        this.id = memberId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    @PreRemove
    public void onDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void allowAllowance(){
        this.allowance = Boolean.TRUE;
    }
}
