package com.yellobook.domains.inform.repository;

import com.yellobook.domains.inform.entity.InformMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformMentionRepository extends JpaRepository<InformMention, Long> {
    List<InformMention> findByInformId(Long informId);
}
