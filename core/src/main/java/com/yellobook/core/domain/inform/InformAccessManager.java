package com.yellobook.core.domain.inform;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class InformAccessManager {
    private final InformReader informReader;
    private final InformMentionProcessor informMentionProcessor;

    public InformAccessManager(InformReader informReader, InformMentionProcessor informMentionProcessor) {
        this.informReader = informReader;
        this.informMentionProcessor = informMentionProcessor;
    }

    public void isAuthorOrMentioned(Long informId, Long memberId) {
        Inform inform = informReader.read(informId);
        if (!inform.author()
                .memberId()
                .equals(memberId) && !informMentionProcessor.isMentioned(informId, memberId)) {
            throw new CoreException(CoreErrorType.INFORM_ACCESS_NOT_ALLOWED);
        }
    }

    public void isAuthor(Long informId, Member author) {
        Inform inform = informReader.read(informId);
        if (!inform.author()
                .equals(author)) {
            throw new CoreException(CoreErrorType.INFORM_AUTHOR_NOT_MATCH);
        }
    }
}
