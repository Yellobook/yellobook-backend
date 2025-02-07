package com.yellobook.storage.db.core;

import com.yellobook.admin.domain.AdminMember;
import com.yellobook.admin.domain.AdminMemberRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AdminMemberCoreRepository implements AdminMemberRepository {
    private final AdminMemberJpaRepository adminMemberJpaRepository;

    public AdminMemberCoreRepository(AdminMemberJpaRepository adminMemberJpaRepository) {
        this.adminMemberJpaRepository = adminMemberJpaRepository;
    }

    @Override
    @Transactional
    public void updateLastLoginAt(Long adminId) {
        adminMemberJpaRepository.updateLastLoginAt(adminId);
    }

    @Override
    public Optional<AdminMember> findByUsername(String username) {
        return adminMemberJpaRepository.findByUsername(username)
                .map(AdminMemberEntity::toAdminMember);
    }

    @Override
    public Optional<AdminMember> findBySub(String sub) {
        return adminMemberJpaRepository.findBySub(sub)
                .map(AdminMemberEntity::toAdminMember);
    }

    @Override
    public Optional<AdminMember> findById(Long adminId) {
        return adminMemberJpaRepository.findById(adminId)
                .map(AdminMemberEntity::toAdminMember);
    }
}
