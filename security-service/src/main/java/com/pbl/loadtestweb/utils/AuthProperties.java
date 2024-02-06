package com.pbl.loadtestweb.utils;

import com.pbl.loadtestweb.common.exception.InternalServerException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthProperties {

  private final Dotenv dotenv;

  public String getTokenSecret() {
    return dotenv.get("TOKEN_SECRET");
  }

  public String getRefreshTokenSecret() {
    return dotenv.get("REFRESH_TOKEN_SECRET");
  }

  public long getTokenExpirationMsec() {
    String tokenExpirationMsecStr = dotenv.get("TOKEN_EXPIRATION_MSEC");
    try {
      return Long.parseLong(tokenExpirationMsecStr);
    } catch (NumberFormatException e) {
      throw new InternalServerException("Invalid value for TOKEN_EXPIRATION_MSEC");
    }
  }

  public long getRefreshTokenExpirationMsec() {
    String tokenExpirationMsecStr = dotenv.get("REFRESH_TOKEN_EXPIRATION_MSEC");
    try {
      return Long.parseLong(tokenExpirationMsecStr);
    } catch (NumberFormatException e) {
      throw new InternalServerException("Invalid value for REFRESH_TOKEN_EXPIRATION_MSEC");
    }
  }
}
