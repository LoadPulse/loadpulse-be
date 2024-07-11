package com.pbl.loadpulse.web.endpoint.auth;

import com.pbl.loadpulse.auth.annotation.CurrentUser;
import com.pbl.loadpulse.auth.domain.UserPrincipal;
import com.pbl.loadpulse.auth.payload.request.RefreshTokenRequest;
import com.pbl.loadpulse.auth.payload.request.SignInRequest;
import com.pbl.loadpulse.auth.payload.request.SignUpRequest;
import com.pbl.loadpulse.auth.service.UserService;
import com.pbl.loadpulse.auth.utils.TokenUtils;
import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.BadRequestException;
import com.pbl.loadpulse.common.exception.UnauthorizedException;
import com.pbl.loadpulse.common.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Auth APIs")
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final UserService userService;

  private final TokenUtils tokenUtils;

  @PostMapping("/sign-up")
  @Operation(summary = "Api sign up user")
  public ResponseEntity<ResponseDataAPI> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userService.signUp(signUpRequest)));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<ResponseDataAPI> signIn(@Valid @RequestBody SignInRequest signInRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  signInRequest.getEmail().toLowerCase(), signInRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

      return ResponseEntity.ok(
          ResponseDataAPI.successWithoutMeta(tokenUtils.createToken(userPrincipal)));
    } catch (BadCredentialsException e) {
      throw new BadRequestException(MessageConstant.INCORRECT_EMAIL_OR_PASSWORD);
    } catch (InternalAuthenticationServiceException e) {
      throw new UnauthorizedException(e.getMessage());
    } catch (DisabledException e) {
      throw new UnauthorizedException(MessageConstant.EMAIL_IS_NOT_VERIFIED);
    } catch (AuthenticationException e) {
      throw new UnauthorizedException(MessageConstant.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/refresh-tokens")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> refreshTokenUser(
      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest,
      @CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            tokenUtils.refreshToken(refreshTokenRequest.getRefreshToken())));
  }

  @GetMapping("/confirm-email")
  @Operation(summary = "API confirm email")
  public ResponseEntity<ResponseDataAPI> confirmEmail(
      @RequestParam(name = "confirmation-token") String confirmationToken) {
    userService.confirmEmail(confirmationToken);
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }
}
