package com.pbl.loadtestweb.service;

import com.pbl.loadtestweb.domain.User;
import com.pbl.loadtestweb.payload.request.SignUpRequest;
import com.pbl.loadtestweb.payload.response.UserResponse;

import java.util.UUID;

public interface UserService {

  UserResponse signUp(SignUpRequest signUpRequest);

  User findUserById(UUID userId);

  UserResponse getUserProfileById(UUID userId);
}
