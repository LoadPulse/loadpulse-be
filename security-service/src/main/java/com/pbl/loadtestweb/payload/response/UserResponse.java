package com.pbl.loadtestweb.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pbl.loadtestweb.domain.enums.EmailValidationStatus;
import com.pbl.loadtestweb.domain.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

  private UUID id;

  private String email;

  private String username;

  private String firstname;

  private String lastname;

  private EmailValidationStatus emailValidationStatus;

  private Role role;
}
