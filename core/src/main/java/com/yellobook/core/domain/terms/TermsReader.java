package com.yellobook.core.domain.terms;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TermsReader {
    private final TermsRepository termsRepository;

    public TermsReader(TermsRepository termsRepository) {
        this.termsRepository = termsRepository;
    }

    public Terms readActive() {
        return termsRepository.findActiveTerms()
                .orElseThrow(() -> new CoreException(CoreErrorType.ACTIVE_TERMS_NOT_FOUND));
    }
}
