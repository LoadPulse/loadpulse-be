package com.pbl.loadtestweb.service.impl;

import com.pbl.loadtestweb.common.constant.MessageConstant;
import com.pbl.loadtestweb.common.exception.BadRequestException;
import com.pbl.loadtestweb.domain.User;
import com.pbl.loadtestweb.mapper.UserMapper;
import com.pbl.loadtestweb.payload.request.SignUpRequest;
import com.pbl.loadtestweb.payload.response.UserResponse;
import com.pbl.loadtestweb.repository.UserRepository;
import com.pbl.loadtestweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Override
  public UserResponse signUp(SignUpRequest signUpRequest) {

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new BadRequestException(MessageConstant.EMAIL_ALREADY_EXISTS);
    }

    if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
      throw new BadRequestException(MessageConstant.PASSWORD_NOT_MATCHED_WITH_CONFIRM_PASSWORD);
    }
    User user = this.save(signUpRequest);

    return userMapper.toUserResponse(user);
  }

  private User save(SignUpRequest signUpRequest) {
    User user = new User();

    user.setEmail(signUpRequest.getEmail());
    user.setUsername(signUpRequest.getUsername());
    user.setFirstname(signUpRequest.getFirstname());
    user.setLastname(signUpRequest.getLastname());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

    return userRepository.save(user);
  }
}
