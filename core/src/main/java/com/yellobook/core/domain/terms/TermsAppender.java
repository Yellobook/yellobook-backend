package com.yellobook.core.domain.terms;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Transactional
@Component
public class TermsAppender {
    private final TermsRepository termsRepository;

    public TermsAppender(TermsRepository termsRepository) {
        this.termsRepository = termsRepository;
    }

    public Long append(Terms terms) {
        return termsRepository.save(terms);
    }
}
