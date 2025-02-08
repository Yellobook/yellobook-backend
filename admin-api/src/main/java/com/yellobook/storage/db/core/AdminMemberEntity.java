package com.yellobook.storage.db.core;

import com.yellobook.admin.domain.AdminMember;
import com.yellobook.admin.security.AdminMemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_members")
public class AdminMemberEntity extends AdminBaseEntity {

    public AdminMemberEntity(String username, String password, String sub, AdminMemberRole role) {
        this.username = username;
        this.password = password;
        this.sub = sub;
        this.role = role;
    }

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String sub;

    @Enumerated(EnumType.STRING)
    private AdminMemberRole role;

    private LocalDateTime lastLoginAt;

    protected AdminMemberEntity() {
    }


    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public AdminMember toAdminMember() {
        return new AdminMember(
                this.getId(),
                username,
                password,
                role,
                sub
        );
    }
}

