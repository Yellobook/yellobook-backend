package com.yellobook.storage.db.core.terms;

import com.yellobook.core.domain.terms.Terms;
import com.yellobook.core.domain.terms.TermsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TermsCoreRepository implements TermsRepository {
    private final TermsJpaRepository termsJpaRepository;
    private final TermsItemJpaRepository termsItemJpaRepository;

    public TermsCoreRepository(TermsJpaRepository termsJpaRepository, TermsItemJpaRepository termsItemJpaRepository) {
        this.termsJpaRepository = termsJpaRepository;
        this.termsItemJpaRepository = termsItemJpaRepository;
    }

    @Override
    public Optional<Terms> findActiveTerms() {
        Optional<TermsEntity> result = termsJpaRepository.findByIsActiveTrue();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        TermsEntity terms = result.get();
        List<TermsItemEntity> termsItems = termsItemJpaRepository.findByTerms(terms);

        return Optional.of(new Terms(
                terms.getId(),
                terms.getName(),
                terms.getVersion(),
                termsItems.stream()
                        .map(TermsItemEntity::toTermsItem)
                        .toList()));
    }
}
