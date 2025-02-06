package com.yellobook.core.domain.terms;

import java.util.Optional;

public interface TermsRepository {
    Optional<Terms> findActiveTerms();
}
