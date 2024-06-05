package com.pbl.loadpulse.service;

import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.NotFoundException;
import com.pbl.loadpulse.domain.User;
import com.pbl.loadpulse.domain.UserPrincipal;
import com.pbl.loadpulse.repository.UserRepository;
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
