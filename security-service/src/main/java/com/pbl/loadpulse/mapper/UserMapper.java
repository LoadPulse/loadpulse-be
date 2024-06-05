package com.pbl.loadpulse.mapper;

import com.pbl.loadpulse.common.config.SpringMapStructConfig;
import com.pbl.loadpulse.domain.User;
import com.pbl.loadpulse.payload.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface UserMapper {

  UserResponse toUserResponse(User user);
}
