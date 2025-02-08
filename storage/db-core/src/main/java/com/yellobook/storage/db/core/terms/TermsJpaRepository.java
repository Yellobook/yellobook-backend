package com.yellobook.storage.db.core.terms;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsJpaRepository extends JpaRepository<TermsEntity, Long> {
    Optional<TermsEntity> findByIsActiveTrue();
}
