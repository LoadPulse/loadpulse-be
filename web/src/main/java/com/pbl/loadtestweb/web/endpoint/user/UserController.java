package com.pbl.loadtestweb.web.endpoint.user;

import com.pbl.loadtestweb.annotation.CurrentUser;
import com.pbl.loadtestweb.common.payload.general.ResponseDataAPI;
import com.pbl.loadtestweb.domain.UserPrincipal;
import com.pbl.loadtestweb.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Api(tags = "User APIs")
public class UserController {

  private final UserService userService;

  @GetMapping("/my-profile")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyProfile(@CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(userService.getUserProfileById(userPrincipal.getId())));
  }
}
