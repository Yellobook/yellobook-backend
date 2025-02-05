package com.yellobook.storage.db.core.terms;

import com.yellobook.core.domain.terms.Terms;
import com.yellobook.core.domain.terms.TermsAgreement;
import com.yellobook.core.domain.terms.TermsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TermsCoreRepository implements TermsRepository {
    private final TermsAgreementJpaRepository termsAgreementJpaRepository;
    private final TermsJpaRepository termsJpaRepository;

    public TermsCoreRepository(TermsAgreementJpaRepository termsAgreementJpaRepository,
                               TermsJpaRepository termsJpaRepository) {
        this.termsAgreementJpaRepository = termsAgreementJpaRepository;
        this.termsJpaRepository = termsJpaRepository;
    }


    @Override
    public Long save(Terms terms) {
        return 1L;
    }

    @Override
    public void agree(TermsAgreement agreement) {

    }

    @Override
    public List<Long> findRequiredTermsItemId(Terms terms) {
        return List.of();
    }

    @Override
    public Optional<Terms> findActiveTerms() {
        return Optional.empty();
    }

    @Override
    public boolean hasMemberAgreedToActiveTerms(Long memberId) {
        return false;
    }

    @Override
    public Optional<Terms> findTermsById(Long id) {
        return Optional.empty();
    }
}
