package com.pbl.loadtestweb.web.endpoint.security;

import com.pbl.loadtestweb.common.payload.general.ResponseDataAPI;
import com.pbl.loadtestweb.payload.request.SignUpRequest;
import com.pbl.loadtestweb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Api(tags = "Auth APIs")
public class AuthController {

  private final UserService userService;

  @PostMapping("/sign-up")
  @ApiOperation("User sign up")
  public ResponseEntity<ResponseDataAPI> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userService.signUp(signUpRequest)));
  }
}
