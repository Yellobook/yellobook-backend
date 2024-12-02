package com.yellobook.core.domains.member.entity;

import com.yellobook.core.core.enums.MemberRole;
import com.yellobook.core.domains.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
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
    public Member(Long memberId, String nickname, String email, String profileImage, MemberRole role,
                  Boolean allowance) {
        this.id = memberId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
        this.allowance = allowance;
    }

    @PreRemove
    public void onDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void updateAllowance() {
        this.allowance = true;
    }
}
