package com.yellobook.domains.inform.repository;

import com.yellobook.domains.inform.entity.InformComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformCommentRepository extends JpaRepository<InformComment, Long> {
}
