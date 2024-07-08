package com.pbl.loadpulse.auth.service;

import com.pbl.loadpulse.auth.domain.User;
import com.pbl.loadpulse.auth.domain.UserPrincipal;
import com.pbl.loadpulse.auth.repository.UserRepository;
import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return UserPrincipal.create(user);
  }
}
