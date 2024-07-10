package com.pbl.loadpulse.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth")
@Getter
@Setter
public class TokenProperties {

  private String tokenSecret;

  private long tokenExpirationMsec;

  private String refreshTokenSecret;

  private long refreshTokenExpirationMsec;
}
