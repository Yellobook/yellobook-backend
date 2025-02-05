package com.yellobook.storage.db.core.inform;

import com.yellobook.core.domain.inform.InformComment;
import com.yellobook.core.domain.inform.InformCommentRepository;
import com.yellobook.core.domain.inform.NewInformComment;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InformCommentCoreRepository implements InformCommentRepository {
    @Override
    public Long save(NewInformComment commend) {
        return 1L;
    }

    @Override
    public List<InformComment> findCommentsByInformId(Long informId) {
        return List.of();
    }
}
