package com.pbl.loadpulse.web.endpoint.user;

import com.pbl.loadpulse.annotation.CurrentUser;
import com.pbl.loadpulse.common.payload.general.ResponseDataAPI;
import com.pbl.loadpulse.domain.UserPrincipal;
import com.pbl.loadpulse.service.UserService;
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
