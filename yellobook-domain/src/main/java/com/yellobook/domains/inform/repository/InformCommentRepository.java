package com.yellobook.domains.inform.repository;

import com.yellobook.domains.inform.entity.InformComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformCommentRepository extends JpaRepository<InformComment, Long> {
    List<InformComment> findByInformId(Long informId);
}
