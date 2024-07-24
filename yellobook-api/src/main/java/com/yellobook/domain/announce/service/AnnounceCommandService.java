package com.yellobook.domain.announce.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.announce.repository.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnounceCommandService {
    private final AnnounceRepository announceRepository;

    public void deleteAnnounce(Long announceId, CustomOAuth2User oAuth2User){

        //announceRepository.deleteById(announceId);
    }

}
