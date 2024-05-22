package com.pbl.loadpulse.domain;

import com.pbl.loadpulse.common.domain.AbstractEntity;
import com.pbl.loadpulse.domain.enums.EmailValidationStatus;
import com.pbl.loadpulse.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String username;

  @Column private String firstname;

  @Column private String lastname;

  @Email @Column private String email;

  @Column private String password;

  @Enumerated(EnumType.STRING)
  private EmailValidationStatus emailValidationStatus;

  @Column private UUID confirmationToken;

  @Column private UUID passwordRecoveryToken;

  @Column private Timestamp requestPasswordRecoveryAt;

  @Column private Timestamp validateEmailAt;

  @Enumerated(EnumType.STRING)
  private Role role;
}
