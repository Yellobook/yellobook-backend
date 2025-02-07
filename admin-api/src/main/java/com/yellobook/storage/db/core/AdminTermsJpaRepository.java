package com.yellobook.storage.db.core;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminTermsJpaRepository extends JpaRepository<AdminTermsEntity, Long> {
    @Query("SELECT MAX(t.version) FROM AdminTermsEntity t WHERE t.isFinalized = true")
    Optional<Integer> findFinalizedLatestTermsVersion();

    Optional<AdminTermsEntity> findByIsActiveTrue();
}
