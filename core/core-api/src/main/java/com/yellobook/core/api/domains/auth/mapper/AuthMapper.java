package com.yellobook.core.api.domains.auth.mapper;

import com.yellobook.core.api.domains.auth.dto.response.AllowanceResponse;
import com.yellobook.core.api.domains.auth.dto.response.RefreshResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AuthMapper {
    AllowanceResponse toAllowanceResponse(String accessToken, String refreshToken);

    RefreshResponse toRefreshResponse(String accessToken);
}
