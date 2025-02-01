package com.yellobook.core.domain.inform;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InformCommentService {
    private final InformCommentReader informCommentReader;
    private final InformCommentWriter informCommentWriter;
    private final InformAccessManager informAccessManager;

    public InformCommentService(InformCommentReader informCommentReader, InformCommentWriter informCommentWriter,
                                InformAccessManager informAccessManager) {
        this.informCommentReader = informCommentReader;
        this.informCommentWriter = informCommentWriter;
        this.informAccessManager = informAccessManager;
    }

    @Transactional
    public Long add(NewInformComment commend) {
        return informCommentWriter.add(commend);
    }


    public List<InformComment> getComments(
            Long informId,
            Long memberId
    ) {
        return informCommentReader.readComments(informId);
    }
}
