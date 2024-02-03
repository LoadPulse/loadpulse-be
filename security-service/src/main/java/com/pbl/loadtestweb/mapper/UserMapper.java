package com.pbl.loadtestweb.mapper;

import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.domain.User;
import com.pbl.loadtestweb.payload.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface UserMapper {

  UserResponse toUserResponse(User user);
}
