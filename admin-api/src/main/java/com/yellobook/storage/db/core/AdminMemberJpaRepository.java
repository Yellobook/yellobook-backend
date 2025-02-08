package com.yellobook.storage.db.core;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AdminMemberJpaRepository extends JpaRepository<AdminMemberEntity, Long> {

    @Modifying
    @Query("UPDATE AdminMemberEntity m SET m.updatedAt= CURRENT_TIMESTAMP WHERE m.id = :adminId")
    void updateLastLoginAt(Long adminId);

    Optional<AdminMemberEntity> findByUsername(String username);
    
    Optional<AdminMemberEntity> findBySub(String sub);
}
