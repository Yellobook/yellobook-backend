package com.yellobook.domains.inform.service;


import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.inform.dto.InformResponse;

public interface InformQueryService {
    InformResponse.GetInformResponseDTO getInformById(CustomOAuth2User oAuth2User, Long informId);
    boolean existInformById(Long informId);
}
