package com.yellobook.storage.db.core.inform;

import com.yellobook.core.domain.inform.Inform;
import com.yellobook.core.domain.inform.InformRepository;
import com.yellobook.core.domain.inform.NewInform;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InformCoreRepository implements InformRepository {
    @Override
    public Long save(NewInform newInform) {
        return 1L;
    }

    @Override
    public void deleteById(Long informId) {

    }

    @Override
    public Boolean existsById(Long informId) {
        return null;
    }

    @Override
    public Optional<Inform> findById(Long informId) {
        return Optional.empty();
    }

    @Override
    public Boolean isMentioned(Long informId, Long memberId) {
        return null;
    }

    @Override
    public void increaseView(Long informId) {

    }
}
