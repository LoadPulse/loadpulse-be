package com.pbl.loadpulse.auth.domain;

import com.pbl.loadpulse.common.domain.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserPrincipal implements UserDetails {

  private UUID id;

  private String email;

  private String password;

  private Role role;

  private Collection<? extends GrantedAuthority> authorities;

  private boolean isConfirmed;

  public UserPrincipal(
      UUID id,
      String email,
      String password,
      Role role,
      Collection<? extends GrantedAuthority> authorities,
      boolean isConfirmed) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
    this.authorities = authorities;
    this.isConfirmed = isConfirmed;
  }

  public static UserPrincipal create(User user) {
    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));

    return new UserPrincipal(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        user.getRole(),
        authorities,
        user.getIsConfirmed());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.isConfirmed;
  }
}
