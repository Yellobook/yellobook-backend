package com.yellobook.storage.db.core.terms;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsItemJpaRepository extends JpaRepository<TermsItemEntity, Long> {
    List<TermsItemEntity> findByTerms(TermsEntity terms);
}
