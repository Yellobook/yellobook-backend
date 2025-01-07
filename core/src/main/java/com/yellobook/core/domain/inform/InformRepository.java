package com.yellobook.core.domain.inform;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface InformRepository {
    Long save(String title, String content, LocalDate plannedDate, Long authorId, List<Long> memberIds);
}
