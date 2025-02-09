package com.yellobook.core.domain.inform;

import org.springframework.stereotype.Component;

@Component
public class InformCommentWriter {
    private final InformCommentRepository informCommentRepository;

    public InformCommentWriter(InformCommentRepository informCommentRepository) {
        this.informCommentRepository = informCommentRepository;
    }

    public Long add(NewInformComment commend) {
        return informCommentRepository.save(commend);
    }
}
