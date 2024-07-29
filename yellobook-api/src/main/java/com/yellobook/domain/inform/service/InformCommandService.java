//package com.yellobook.domain.inform.service;
//
//import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
//import com.yellobook.domain.inform.dto.InformCommentRequest;
//import com.yellobook.domain.inform.dto.InformCommentResponse;
//import com.yellobook.domain.inform.dto.InformRequest;
//import com.yellobook.domains.inform.entity.Inform;
//import com.yellobook.domains.inform.entity.InformComment;
//
//public interface InformCommandService {
//    Inform createInform(Long memberId, InformRequest.CreateInformRequestDTO request);
//    void deleteInform(Long informId, CustomOAuth2User oAuth2User);
//    InformCommentResponse.PostCommentResponseDTO createComment(Long informId, CustomOAuth2User oauth2User, InformCommentRequest.PostCommentRequestDTO request);
//}
