package com.pbl.loadtestweb.service;

import com.pbl.loadtestweb.common.constant.MessageConstant;
import com.pbl.loadtestweb.common.exception.NotFoundException;
import com.pbl.loadtestweb.domain.User;
import com.pbl.loadtestweb.domain.UserPrincipal;
import com.pbl.loadtestweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return UserPrincipal.create(user);
  }
}
