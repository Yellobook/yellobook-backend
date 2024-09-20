package com.yellobook.domains.inform.repository;

import com.yellobook.domains.inform.entity.InformMention;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformMentionRepository extends JpaRepository<InformMention, Long> {
    List<InformMention> findAllByInformId(Long informId);
}
