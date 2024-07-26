package com.yellobook.domain.inform.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inform.dto.InformCommentRequest;
import com.yellobook.domain.inform.dto.InformRequest;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;

public interface InformCommandService {
    InformResponse.CreateInformResponseDTO createInform(CustomOAuth2User oAuth2User, InformRequest.CreateInformRequestDTO request);
    void deleteInform(Long informId, CustomOAuth2User oAuth2User);
    InformComment createComment(Long memberId, CustomOAuth2User oauth2User, InformCommentRequest.PostCommentRequestDTO request);
}
