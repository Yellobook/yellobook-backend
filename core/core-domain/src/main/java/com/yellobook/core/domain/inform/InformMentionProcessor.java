package com.yellobook.core.domain.inform;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InformMentionProcessor {
    private final InformRepository informRepository;

    public InformMentionProcessor(InformRepository informRepository) {
        this.informRepository = informRepository;
    }

    public Boolean isMentioned(Long informId, Long memberId) {
        return informRepository.isMentioned(informId, memberId);
    }

    public void mention(Long informId, List<Long> mentionIds) {

    }
}
