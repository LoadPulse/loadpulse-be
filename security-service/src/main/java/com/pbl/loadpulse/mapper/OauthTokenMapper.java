package com.pbl.loadpulse.mapper;

import com.pbl.loadpulse.common.config.SpringMapStructConfig;
import com.pbl.loadpulse.payload.response.OauthTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;

@Mapper(config = SpringMapStructConfig.class)
public interface OauthTokenMapper {

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(source = "expiresIn", target = "expiresIn")
  @Mapping(source = "createdAt", target = "createdAt")
  OauthTokenResponse toOauthTokenResponse(
      String accessToken, String refreshToken, long expiresIn, Timestamp createdAt);
}
