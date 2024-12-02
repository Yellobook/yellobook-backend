package com.yellobook.core.api.domains.inform.service;

import com.yellobook.core.api.domains.inform.dto.response.GetInformResponse;
import com.yellobook.core.domains.inform.entity.Inform;
import com.yellobook.core.domains.inform.entity.InformComment;
import com.yellobook.core.domains.inform.entity.InformMention;
import com.yellobook.core.api.domains.inform.mapper.InformMapper;
import com.yellobook.core.domains.inform.repository.InformCommentRepository;
import com.yellobook.core.domains.inform.repository.InformMentionRepository;
import com.yellobook.core.domains.inform.repository.InformRepository;
import com.yellobook.core.domains.member.entity.Member;
import com.yellobook.core.api.support.error.code.InformErrorCode;
import com.yellobook.core.api.support.error.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InformQueryService {
    private final InformRepository informRepository;
    private final InformMentionRepository informMentionRepository;
    private final InformCommentRepository informCommentRepository;
    private final InformMapper informMapper;

    public GetInformResponse getInformById(Long memberId, Long informId) {
        Inform inform = informRepository.findById(informId)
                .orElseThrow(() -> new CustomException(InformErrorCode.INFORM_NOT_FOUND));
        Long writerId = inform.getMember()
                .getId();

        // 공지의 언급들
        List<InformMention> mentions = informMentionRepository.findAllByInformId(informId);

        // 공지에 달린 댓글들
        List<InformComment> comments = inform.getComments();

        List<Member> mentionedMembers = new ArrayList<>();

        mentions.stream()
                .map(InformMention::getMember)
                .forEach(mentionedMembers::add);

        boolean isMentioned = mentionedMembers.stream()
                .anyMatch(mentionedMember -> mentionedMember.getId()
                        .equals(memberId));

        if (memberId.equals(writerId) || isMentioned) {
            return informMapper.toGetInformResponseDTO(inform, comments, mentionedMembers);
        } else {
            throw new CustomException(InformErrorCode.NOT_MENTIONED);
        }
    }
}
