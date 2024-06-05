package com.pbl.loadpulse.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pbl.loadpulse.common.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignInRequest {

  @NotBlank @Email private String email;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String password;
}
