package com.pbl.loadpulse.service;

import com.pbl.loadpulse.domain.User;
import com.pbl.loadpulse.payload.request.SignUpRequest;
import com.pbl.loadpulse.payload.response.UserResponse;

import java.util.UUID;

public interface UserService {

  UserResponse signUp(SignUpRequest signUpRequest);

  User findUserById(UUID userId);

  UserResponse getUserProfileById(UUID userId);
}
