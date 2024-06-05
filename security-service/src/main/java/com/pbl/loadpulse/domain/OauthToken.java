package com.pbl.loadpulse.domain;

import com.pbl.loadpulse.common.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "oauth_tokens")
public class OauthToken extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column private UUID refreshToken;

  @Column private Timestamp revokedAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
