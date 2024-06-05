package com.pbl.loadpulse.service.impl;

import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.BadRequestException;
import com.pbl.loadpulse.common.exception.NotFoundException;
import com.pbl.loadpulse.domain.User;
import com.pbl.loadpulse.domain.enums.Role;
import com.pbl.loadpulse.mapper.UserMapper;
import com.pbl.loadpulse.payload.request.SignUpRequest;
import com.pbl.loadpulse.payload.response.UserResponse;
import com.pbl.loadpulse.repository.UserRepository;
import com.pbl.loadpulse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

  @Override
  public User findUserById(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }

  @Override
  public UserResponse getUserProfileById(UUID userId) {
    User user = this.findUserById(userId);
    return userMapper.toUserResponse(user);
  }

  private User save(SignUpRequest signUpRequest) {
    User user = new User();

    user.setEmail(signUpRequest.getEmail());
    user.setUsername(signUpRequest.getUsername());
    user.setFirstname(signUpRequest.getFirstname());
    user.setLastname(signUpRequest.getLastname());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    user.setRole(Role.ROLE_USER);

    return userRepository.save(user);
  }
}
