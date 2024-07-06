package com.pbl.loadpulse.web.endpoint.auth;

import com.pbl.loadpulse.auth.payload.request.SignInRequest;
import com.pbl.loadpulse.auth.payload.request.SignUpRequest;
import com.pbl.loadpulse.auth.service.UserService;
import com.pbl.loadpulse.common.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Auth APIs")
public class AuthController {

  private final UserService userService;

  @PostMapping("/sign-up")
  @Operation(summary = "Api sign up user")
  public ResponseEntity<ResponseDataAPI> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userService.signUp(signUpRequest)));
  }
  @PostMapping("/sign-in")
  @Operation(summary = "Api sign in user")
  public ResponseEntity<ResponseDataAPI> signIn(@Valid @RequestBody SignInRequest signInRequest) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userService.signIn(signInRequest)));
  }
}
