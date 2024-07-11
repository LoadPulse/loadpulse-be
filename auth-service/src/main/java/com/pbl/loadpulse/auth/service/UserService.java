package com.pbl.loadpulse.auth.service;

import com.pbl.loadpulse.auth.domain.User;
import com.pbl.loadpulse.auth.payload.request.SignUpRequest;
import com.pbl.loadpulse.auth.payload.response.UserInfoResponse;

import java.util.UUID;

public interface UserService {

  UserInfoResponse signUp(SignUpRequest signUpRequest);

  User findById(UUID id);

  void confirmEmail(String confirmationToken);
}
