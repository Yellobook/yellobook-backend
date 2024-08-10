package com.yellobook.domains.auth.mapper;

import com.yellobook.domains.auth.dto.response.AllowanceResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AuthMapper {
    AllowanceResponse toAllowanceResponse(String accessToken, String refreshToken);
}
