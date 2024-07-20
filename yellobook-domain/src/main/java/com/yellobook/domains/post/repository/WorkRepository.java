package com.yellobook.domains.post.repository;

import com.yellobook.domains.post.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
}
