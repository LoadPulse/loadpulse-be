package com.pbl.loadpulse.auth.mapper;

import com.pbl.loadpulse.auth.domain.User;
import com.pbl.loadpulse.auth.payload.response.UserInfoResponse;
import com.pbl.loadpulse.common.config.SpringMapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface UserMapper {

  @Mapping(source = "user.id", target = "id")
  @Mapping(source = "user.email", target = "email")
  UserInfoResponse toUserInfoResponse(User user);
}
