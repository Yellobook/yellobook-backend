package com.yellobook.core.domain.terms;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TermsReader {
    private final TermsRepository termsRepository;

    public TermsReader(TermsRepository termsRepository) {
        this.termsRepository = termsRepository;
    }

    public Terms read(Long termsId) {
        return termsRepository.findTermsById(termsId)
                .orElseThrow(
                        () -> new CoreException(CoreErrorType.TERMS_NOT_FOUND)
                );
    }

    public Optional<Terms> readActive() {
        return termsRepository.findActiveTerms();
    }
}
