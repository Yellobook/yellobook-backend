package com.yellobook.core.domain.inform;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InformWriter {
    private final InformRepository informRepository;

    public InformWriter(ImformRepository informRepository) {
        this.informRepository = informRepository;
    }

    public Long createInform(String title, String content, List<Long> mentionedMemberIds) {
        return informRepository.save()
    }

}
