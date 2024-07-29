package com.yellobook.domain.inform.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inform.dto.InformCommentRequest;
import com.yellobook.domain.inform.dto.InformRequest;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InformCommandServiceImpl implements InformCommandService {
    private final InformRepository informRepository;
    @Override
    public Inform createInform(Long memberId, InformRequest.CreateInformRequestDTO request){
        return null;
    }

    @Override
    public void deleteInform(Long informId, CustomOAuth2User oAuth2User){

    }

    @Override
    public InformComment createComment(Long memberId, CustomOAuth2User oauth2User, InformCommentRequest.PostCommentRequestDTO request){
        return null;
    }
}