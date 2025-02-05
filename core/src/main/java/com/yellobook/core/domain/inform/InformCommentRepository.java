package com.yellobook.core.domain.inform;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface InformCommentRepository {
    Long save(NewInformComment commend);

    List<InformComment> findCommentsByInformId(Long informId);
}
