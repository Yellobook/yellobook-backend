package com.yellobook.domain.announce.service;

import com.yellobook.domain.announce.dto.AnnounceCommentRequest;
import com.yellobook.domain.announce.dto.AnnounceRequest;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.announce.entity.Announce;
import com.yellobook.domains.announce.entity.AnnounceComment;
import com.yellobook.domains.announce.repository.AnnounceCommentRepository;
import com.yellobook.domains.announce.repository.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnounceCommandServiceImpl implements AnnounceCommandService {
    private final AnnounceRepository announceRepository;
    private final AnnounceCommentRepository announceCommentRepository;

    @Override
    public Announce createAnnounce(Long memberId, AnnounceRequest.PostAnnounceRequestDTO request){
        return null;
    }

    @Override
    public void deleteAnnounce(Long memberId, CustomOAuth2User oauth2User){

    }

    @Override
    public AnnounceComment createComment(Long memberId, CustomOAuth2User oauth2User, AnnounceCommentRequest.PostCommentRequestDTO request){
        return null;
    }
}
