package com.pbl.loadpulse.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        //                .exceptionHandling(exp -> exp.authenticationEntryPoint(new
        // RestAuthenticationEntryPoint()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        antMatcher("/app/**"),
                        antMatcher("/favicon.ico"),
                        antMatcher("/**/*.png"),
                        antMatcher("/**/*.jpg"),
                        antMatcher("/**/*.css"),
                        antMatcher("/**/*.js"),
                        antMatcher("/swagger-ui/**"),
                        antMatcher("/v3/api-docs/**"),
                        antMatcher("/v1/user"))
                    .permitAll()
                    .anyRequest()
                    .permitAll())
        .oauth2Login(
            oauth2Login ->
                oauth2Login.userInfoEndpoint(
                    userInfoEndpoint -> userInfoEndpoint.oidcUserService(this.oidcUserService())));
    //        http.addFilterBefore(tokenAuthenticationFilter,

    return http.build();
  }

  private OidcUserService oidcUserService() {
    OidcUserService delegate = new OidcUserService();
    return new OidcUserService() {
      @Override
      public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        return delegate.loadUser(userRequest);
      }
    };
  }
}
