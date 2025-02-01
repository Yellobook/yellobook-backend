package com.yellobook.core.domain.inform;

import org.springframework.stereotype.Component;

@Component
public class InformViewProcessor {
    private final InformRepository informRepository;

    public InformViewProcessor(InformRepository informRepository) {
        this.informRepository = informRepository;
    }

    public void increase(Long informId) {
        informRepository.increaseView(informId);
    }
}
