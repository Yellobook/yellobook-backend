package com.yellobook.domains.inform.service;

import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.inform.dto.InformCommentRequest;
import com.yellobook.domains.inform.dto.InformCommentResponse;
import com.yellobook.domains.inform.dto.InformRequest;
import com.yellobook.domains.inform.dto.InformResponse;

public interface InformCommandService {
    InformResponse.CreateInformResponseDTO createInform(CustomOAuth2User oAuth2User, InformRequest.CreateInformRequestDTO request);
    InformResponse.RemoveInformResponseDTO deleteInform(Long informId, CustomOAuth2User oAuth2User);
    InformCommentResponse.PostCommentResponseDTO addComment(Long informId, CustomOAuth2User oauth2User, InformCommentRequest.PostCommentRequestDTO request);
}
