package com.pbl.loadpulse.auth.mapper;

import com.pbl.loadpulse.auth.payload.response.JwtResponse;
import com.pbl.loadpulse.common.config.SpringMapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface TokenMapper {
  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  JwtResponse toJwtResponse(String accessToken, String refreshToken);
}
