package com.pbl.loadtestweb.service.impl;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.MessageConstant;
import com.pbl.loadtestweb.common.exception.ForBiddenException;
import com.pbl.loadtestweb.common.exception.NotFoundException;
import com.pbl.loadtestweb.domain.OauthToken;
import com.pbl.loadtestweb.repository.OauthTokenRepository;
import com.pbl.loadtestweb.service.OauthTokenService;
import com.pbl.loadtestweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OauthTokenServiceImpl implements OauthTokenService {

  private final OauthTokenRepository oauthTokenRepository;

  private final UserService userService;

  @Override
  public OauthToken createToken(UUID userId, UUID refreshToken) {
    try {
      OauthToken oauthToken = new OauthToken();
      oauthToken.setUser(userService.findUserById(userId));
      oauthToken.setRefreshToken(Objects.requireNonNullElseGet(refreshToken, UUID::randomUUID));
      return oauthTokenRepository.save(oauthToken);
    } catch (Exception e) {
      throw new ForBiddenException(MessageConstant.INVALID_REFRESH_TOKEN);
    }
  }

  @Override
  public OauthToken getOauthTokenById(UUID id) {
    return oauthTokenRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MessageConstant.OAUTH_TOKEN_NOT_FOUND));
  }

  @Override
  public OauthToken getOauthTokenByRefreshToken(UUID refreshTokenId) {
    OauthToken oauthToken =
        oauthTokenRepository
            .findByRefreshToken(refreshTokenId)
            .orElseThrow(() -> new NotFoundException(MessageConstant.OAUTH_TOKEN_NOT_FOUND));
    if (oauthToken.getRevokedAt() != null) {
      throw new ForBiddenException(MessageConstant.REVOKED_TOKEN);
    }
    oauthToken.setRevokedAt(CommonFunction.getCurrentDateTime());
    oauthToken.setRefreshToken(null);
    return oauthTokenRepository.save(oauthToken);
  }

  @Override
  public void revoke(OauthToken oauthToken) {
    // TODO
  }

  @Override
  public void revokeAll(OauthToken oauthToken, String email) {
    // TODO
  }
}
