package com.pbl.loadpulse.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {

  @Email private String email;

  private String username;

  private String firstname;

  @NotBlank
  private String lastname;

  @NotBlank
  private String password;

  private String confirmPassword;
}
