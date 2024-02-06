package com.pbl.loadtestweb.repository;

import com.pbl.loadtestweb.domain.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, UUID> {

  Optional<OauthToken> findByRefreshToken(UUID refreshToken);
}
