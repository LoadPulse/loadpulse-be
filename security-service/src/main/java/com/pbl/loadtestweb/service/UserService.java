package com.pbl.loadtestweb.service;

import com.pbl.loadtestweb.payload.request.SignUpRequest;
import com.pbl.loadtestweb.payload.response.UserResponse;

public interface UserService {

  UserResponse signUp(SignUpRequest signUpRequest);
}
