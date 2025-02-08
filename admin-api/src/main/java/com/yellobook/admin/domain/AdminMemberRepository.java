package com.yellobook.admin.domain;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMemberRepository {

    Optional<AdminMember> findByUsername(String username);

    Optional<AdminMember> findBySub(String sub);

    Optional<AdminMember> findById(Long adminId);

    void updateLastLoginAt(Long adminId);
}
