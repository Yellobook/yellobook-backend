package com.yellobook.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_members")
public class AdminMemberEntity extends AdminBaseEntity {
    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDateTime lastLoginAt;

    protected AdminMemberEntity() {
    }

    public AdminMemberEntity(String email, String password, LocalDateTime lastLoginAt) {
        this.email = email;
        this.password = password;
        this.lastLoginAt = lastLoginAt;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}

