package com.yellobook.domain.auth.mapper;

import com.yellobook.domain.auth.dto.response.AllowanceResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AuthMapper {
    AllowanceResponse toAllowanceResponse(String accessToken, String refreshToken);
}
