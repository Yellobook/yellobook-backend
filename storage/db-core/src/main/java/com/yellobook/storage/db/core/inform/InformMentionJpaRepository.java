package com.yellobook.storage.db.core.inform;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformMentionJpaRepository extends JpaRepository<InformMentionEntity, Long> {
    List<InformMentionEntity> findAllByInformId(Long informId);

    void deleteByInformId(Long informId);
}
