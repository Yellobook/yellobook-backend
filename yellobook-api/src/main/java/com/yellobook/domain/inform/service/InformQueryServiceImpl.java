package com.yellobook.domain.inform.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inform.dto.InformCommentResponse;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domain.inform.mapper.InformMapper;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.repository.InformCommentRepository;
import com.yellobook.domains.inform.repository.InformMentionRepository;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.error.code.InformErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InformQueryServiceImpl implements InformQueryService {
    private final InformRepository informRepository;
    private final InformMentionRepository informMentionRepository;
    private final InformCommentRepository informCommentRepository;
    private final InformMapper informMapper;

    @Override
    public InformResponse.GetInformResponseDTO getInformById(CustomOAuth2User oAuth2User, Long informId){
        Long memberId = oAuth2User.getMemberId();
        Inform inform = informRepository.findById(informId).orElseThrow(()
                -> new CustomException(InformErrorCode.INFORM_NOT_FOUND));
        List<InformMention> mentions = getMentionsByInformId(informId);
        List<InformComment> comments = getCommentByInformId(informId);
        boolean isMentioned = mentions.stream().anyMatch(mention -> mention.getMember().getId().equals(memberId));

        if (isMentioned) {
            return informMapper.toGetInformResponseDTO(inform, comments, mentions);
        }else {
            throw new CustomException(InformErrorCode.NOT_MENTIONED);
        }
    }
    public List<InformMention> getMentionsByInformId(Long informId) {
        return informMentionRepository.findByInformId(informId);
    }

    public List<InformComment> getCommentByInformId(Long informId){
        return informCommentRepository.findByInformId(informId);
    }

    @Override
    public boolean existInformById(Long informId) {
        return informRepository.existsById(informId);
    }
}
