package com.pbl.loadpulse.service;

import com.pbl.loadpulse.domain.OauthToken;

import java.util.UUID;

public interface OauthTokenService {

  OauthToken createToken(UUID userId, UUID refreshToken);

  OauthToken getOauthTokenById(UUID id);

  OauthToken getOauthTokenByRefreshToken(UUID refreshTokenId);

  void revoke(OauthToken oauthToken);

  void revokeAll(OauthToken oauthToken, String email);
}
