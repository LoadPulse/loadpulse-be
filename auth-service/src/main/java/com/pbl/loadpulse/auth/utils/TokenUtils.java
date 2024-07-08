package com.pbl.loadpulse.auth.utils;

import com.pbl.loadpulse.auth.config.TokenProperties;
import com.pbl.loadpulse.auth.domain.User;
import com.pbl.loadpulse.auth.domain.UserPrincipal;
import com.pbl.loadpulse.auth.mapper.TokenMapper;
import com.pbl.loadpulse.auth.payload.response.TokenResponse;
import com.pbl.loadpulse.auth.service.UserService;
import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.ForBiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenUtils {

  private final TokenProperties tokenProperties;

  private final UserService userService;

  private final TokenMapper tokenMapper;

  public TokenResponse createToken(UserPrincipal userPrincipal) {
    return tokenMapper.toOauthAccessTokenResponse(
        this.createAccessToken(userPrincipal.getId()),
        this.createRefreshToken(userPrincipal.getId()),
        tokenProperties.getTokenExpirationMsec() / 1000);
  }

  public TokenResponse refreshToken(String refreshToken) {
    this.validateRefreshToken(refreshToken, tokenProperties.getRefreshTokenSecret());

    return tokenMapper.toOauthAccessTokenResponse(
        this.createAccessToken(
            this.getUUIDFromToken(refreshToken, tokenProperties.getRefreshTokenSecret())),
        refreshToken,
        tokenProperties.getTokenExpirationMsec() / 1000);
  }

  public User getUserFromToken(String token) {
    this.validateAccessToken(token, tokenProperties.getTokenSecret());
    return userService.findById(this.getUUIDFromToken(token, tokenProperties.getTokenSecret()));
  }

  private UUID getUUIDFromToken(String token, String secret) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return UUID.fromString(claims.getSubject());
  }

  private String createAccessToken(UUID userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenProperties.getTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenProperties.getTokenSecret())
        .compact();
  }

  private String createRefreshToken(UUID userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenProperties.getRefreshTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenProperties.getRefreshTokenSecret())
        .compact();
  }

  private void validateAccessToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForBiddenException(MessageConstant.EXPIRED_TOKEN);
    } catch (Exception ex) {
      log.info(ex.getMessage());
      throw new ForBiddenException(MessageConstant.INVALID_TOKEN);
    }
  }

  private void validateRefreshToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForBiddenException(MessageConstant.EXPIRED_REFRESH_TOKEN);
    } catch (Exception ex) {
      throw new ForBiddenException(MessageConstant.INVALID_REFRESH_TOKEN);
    }
  }
}
