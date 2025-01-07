package com.yellobook.core.domain.inform;

import org.springframework.stereotype.Component;

@Component
public class InformReader {
    private final InformRepository informRepository;

    public InformReader(InformRepository informRepository) {
        this.informRepository = informRepository;
    }
}
