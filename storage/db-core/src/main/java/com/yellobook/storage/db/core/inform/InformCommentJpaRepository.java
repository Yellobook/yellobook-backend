package com.yellobook.storage.db.core.inform;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformCommentJpaRepository extends JpaRepository<InformCommentEntity, Long> {
    List<InformCommentEntity> findByInformId(Long informId);
}
