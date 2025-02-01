package com.yellobook.storage.db.core.member;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.ProfileInfo;
import com.yellobook.core.domain.member.SocialInfo;
import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "members",
        indexes = {
                @Index(name = "ix_members_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_members_email", columnNames = "email")
        }
)
@SQLDelete(sql = "UPDATE members SET is_deleted = 1, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "is_deleted = 0")
public class MemberEntity extends BaseEntity {
    @Column(nullable = false, length = 20)
    private String nickname;

    private String bio;

    private String profileImage;

    private String oauthId;

    private String oauthProvider;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AppMemberRole role;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime nicknameUpdatedAt;

    private LocalDateTime deletedAt;

    protected MemberEntity() {
    }

    public MemberEntity(String nickname, String bio, String email, String profileImage, String oauthId,
                        String oauthProvider, AppMemberRole role) {
        this.nickname = nickname;
        this.bio = bio;
        this.email = email;
        this.profileImage = profileImage;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.role = role;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.nicknameUpdatedAt = LocalDateTime.now();
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public Member toMember() {
        return new Member(
                this.getId(),
                new ProfileInfo(
                        nickname,
                        bio,
                        profileImage,
                        nicknameUpdatedAt
                ),
                new SocialInfo(
                        oauthId,
                        oauthProvider,
                        email
                )
        );
    }

    public String getNickname() {
        return nickname;
    }

    public String getBio() {
        return bio;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public String getEmail() {
        return email;
    }

    public AppMemberRole getRole() {
        return role;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getNicknameUpdatedAt() {
        return nicknameUpdatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
