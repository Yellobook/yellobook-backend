package com.yellobook.domains.inform.service;

import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.mapper.InformMapper;
import com.yellobook.domains.inform.repository.InformCommentRepository;
import com.yellobook.domains.inform.repository.InformMentionRepository;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.error.code.InformErrorCode;
import com.yellobook.error.exception.CustomException;
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

        boolean isMentioned = mentions.stream()
                .map(InformMention::getMember)
                .peek(mentionedMembers::add)
                .anyMatch(mentionedMember -> mentionedMember.getId()
                        .equals(memberId));

        if (memberId.equals(writerId) || isMentioned) {
            return informMapper.toGetInformResponseDTO(inform, comments, mentionedMembers);
        } else {
            throw new CustomException(InformErrorCode.NOT_MENTIONED);
        }
    }
}
