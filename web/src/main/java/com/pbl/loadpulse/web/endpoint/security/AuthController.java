package com.pbl.loadpulse.web.endpoint.security;

import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.payload.general.ResponseDataAPI;
import com.pbl.loadpulse.domain.UserPrincipal;
import com.pbl.loadpulse.domain.enums.Role;
import com.pbl.loadpulse.payload.request.RefreshTokenRequest;
import com.pbl.loadpulse.payload.request.SignInRequest;
import com.pbl.loadpulse.payload.request.SignUpRequest;
import com.pbl.loadpulse.service.UserService;
import com.pbl.loadpulse.utils.TokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  private final TokenProvider tokenProvider;

  private final AuthenticationManager authenticationManager;

  @PostMapping("/sign-up")
  @ApiOperation("User sign up")
  public ResponseEntity<ResponseDataAPI> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userService.signUp(signUpRequest)));
  }

  @PostMapping("/sign-in")
  @ApiOperation("Sign in")
  public ResponseEntity<ResponseDataAPI> signIn(@RequestBody @Valid SignInRequest signInRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  signInRequest.getEmail().toLowerCase(), signInRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      if (userPrincipal.getRole().equals(Role.ROLE_ADMIN)) {
        throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
      }

      return ResponseEntity.ok(
          ResponseDataAPI.successWithoutMeta(tokenProvider.createOauthToken(userPrincipal)));

    } catch (BadCredentialsException ex) {
      throw new BadRequestException(MessageConstant.INCORRECT_EMAIL_OR_PASSWORD);
    }
  }

  @PostMapping("/refresh-token")
  @ApiOperation("Refresh token")
  public ResponseEntity<ResponseDataAPI> refreshToken(
      @RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            tokenProvider.refreshTokenOauthToken(refreshTokenRequest.getRefreshToken(), false)));
  }
}
