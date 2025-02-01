package com.yellobook.core.domain.inform;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InformCommentReader {
    private final InformCommentRepository informCommentRepository;

    public InformCommentReader(InformCommentRepository informCommentRepository) {
        this.informCommentRepository = informCommentRepository;
    }

    public List<InformComment> readComments(Long informId) {
        return informCommentRepository.findCommentsByInformId(informId);
    }
}
