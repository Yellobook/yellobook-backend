package com.yellobook.core.domain.inform;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface InformRepository {
    Long save(NewInform newInform);

    void deleteById(Long informId);

    Boolean existsById(Long informId);

    Optional<Inform> findById(Long informId);

    Boolean isMentioned(Long informId, Long memberId);

    void increaseView(Long informId);
}
