package com.yellobook.domains.work.repository;

import com.yellobook.domains.work.entity.WorkComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkCommentRepository extends JpaRepository<WorkComment,Long> {
}
