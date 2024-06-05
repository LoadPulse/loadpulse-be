package com.pbl.loadpulse.utils;

import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.ForBiddenException;
import com.pbl.loadpulse.domain.OauthToken;
import com.pbl.loadpulse.domain.User;
import com.pbl.loadpulse.domain.UserPrincipal;
import com.pbl.loadpulse.domain.enums.Role;
import com.pbl.loadpulse.mapper.OauthTokenMapper;
import com.pbl.loadpulse.payload.response.OauthTokenResponse;
import com.pbl.loadpulse.service.OauthTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final AuthProperties authProperties;

  private final OauthTokenService oauthTokenService;

  private final OauthTokenMapper oauthTokenMapper;

  private String createAccessToken(OauthToken oauthToken) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + authProperties.getTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(oauthToken.getId().toString())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, authProperties.getTokenSecret())
        .compact();
  }

  private String createRefreshToken(OauthToken oauthToken) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + authProperties.getRefreshTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(oauthToken.getRefreshToken().toString())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, authProperties.getRefreshTokenSecret())
        .compact();
  }

  private void validateAccessToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForBiddenException(MessageConstant.EXPIRED_TOKEN);
    } catch (Exception ex) {
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

  public User getUserFromToken(String token) {
    return this.getOauthTokenByToken(token).getUser();
  }

  public OauthToken getOauthTokenByToken(String token) {
    this.validateAccessToken(token, authProperties.getTokenSecret());
    OauthToken oauthToken =
        oauthTokenService.getOauthTokenById(
            this.getUUIDFromToken(token, authProperties.getTokenSecret()));
    if (oauthToken.getRevokedAt() != null) {
      throw new ForBiddenException(MessageConstant.REVOKED_TOKEN);
    }
    return oauthToken;
  }

  private UUID getUUIDFromToken(String token, String secret) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return UUID.fromString(claims.getSubject());
  }

  public OauthTokenResponse createToken(UUID userId) {
    OauthToken oauthToken = oauthTokenService.createToken(userId, null);
    return oauthTokenMapper.toOauthTokenResponse(
        this.createAccessToken(oauthToken),
        this.createRefreshToken(oauthToken),
        authProperties.getTokenExpirationMsec() / 1000,
        oauthToken.getCreatedAt());
  }

  public OauthTokenResponse createOauthToken(UserPrincipal userPrincipal) {
    return this.createToken(userPrincipal.getId());
  }

  public OauthTokenResponse refreshTokenOauthToken(String refreshToken, boolean isAdmin) {
    this.validateRefreshToken(refreshToken, authProperties.getRefreshTokenSecret());
    UUID refreshTokenId =
        this.getUUIDFromToken(refreshToken, authProperties.getRefreshTokenSecret());

    OauthToken oauthToken = oauthTokenService.getOauthTokenByRefreshToken(refreshTokenId);
    if (isAdmin && !oauthToken.getUser().getRole().equals(Role.ROLE_ADMIN)
        || !isAdmin && oauthToken.getUser().getRole().equals(Role.ROLE_ADMIN)) {
      throw new ForBiddenException(MessageConstant.FORBIDDEN);
    }
    OauthToken result = oauthTokenService.createToken(oauthToken.getUser().getId(), refreshTokenId);
    return oauthTokenMapper.toOauthTokenResponse(
        this.createAccessToken(result),
        refreshToken,
        authProperties.getTokenExpirationMsec() / 1000,
        result.getCreatedAt());
  }
}
