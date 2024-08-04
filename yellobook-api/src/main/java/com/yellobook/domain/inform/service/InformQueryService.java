package com.yellobook.domain.inform.service;


import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inform.dto.InformCommentResponse;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domains.inform.entity.InformComment;

import java.util.List;

public interface InformQueryService {
    InformResponse.GetInformResponseDTO getInformById(CustomOAuth2User oAuth2User, Long informId);
    boolean existInformById(Long informId);
}
