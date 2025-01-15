package com.yellobook.inform;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformCommentRepository extends JpaRepository<InformComment, Long> {
    List<InformComment> findByInformId(Long informId);
}