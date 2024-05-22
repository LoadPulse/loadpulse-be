package com.pbl.loadpulse.domain;

import com.pbl.loadpulse.domain.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {
  private UUID id;
  private String email;
  private String password;
  private Role role;
  private Collection<? extends GrantedAuthority> authorities;

  public static UserPrincipal create(User user) {

    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));

    return new UserPrincipal(
        user.getId(), user.getEmail(), user.getPassword(), user.getRole(), authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public UUID getId() {
    return id;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
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
    return true;
  }
}
