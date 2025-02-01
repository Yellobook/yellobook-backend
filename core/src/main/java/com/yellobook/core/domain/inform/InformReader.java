package com.yellobook.core.domain.inform;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class InformReader {
    private final InformRepository informRepository;

    public InformReader(InformRepository informRepository) {
        this.informRepository = informRepository;
    }

    public Inform read(Long informId) {
        return informRepository.findById(informId)
                .orElseThrow(() -> new CoreException(CoreErrorType.INFORM_NOT_FOUND));
    }

    public Boolean exist(Long informId) {
        return informRepository.existsById(informId);
    }
}
