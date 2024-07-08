package com.pbl.loadpulse.auth.domain;

import com.pbl.loadpulse.common.domain.AbstractEntity;
import com.pbl.loadpulse.common.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column private Timestamp confirmedAt;

  @Column private Boolean isConfirmed = false;

  @Enumerated(EnumType.STRING)
  @Column
  private Role role;
}
