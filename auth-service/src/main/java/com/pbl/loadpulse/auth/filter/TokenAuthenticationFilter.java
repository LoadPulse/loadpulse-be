package com.pbl.loadpulse.auth.filter;

import com.pbl.loadpulse.auth.domain.User;
import com.pbl.loadpulse.auth.domain.UserPrincipal;
import com.pbl.loadpulse.auth.utils.LogUtils;
import com.pbl.loadpulse.auth.utils.TokenUtils;
import com.pbl.loadpulse.common.common.CommonFunction;
import com.pbl.loadpulse.common.constant.CommonConstant;
import com.pbl.loadpulse.common.constant.MessageConstant;
import com.pbl.loadpulse.common.exception.ForBiddenException;
import com.pbl.loadpulse.common.payload.general.ResponseDataAPI;
import com.pbl.loadpulse.common.payload.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenUtils tokenUtils;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException {
    try {
      String jwt = getJwtFromRequest(request);
      if (StringUtils.hasText(jwt)) {
        User user = tokenUtils.getUserFromToken(jwt);
        UserDetails userDetails = UserPrincipal.create(user);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
    } catch (ForBiddenException ex) {
      LogUtils.error(request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
      ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
      response
          .getWriter()
          .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    } catch (Exception ex) {
      LogUtils.error(request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.INTERNAL_SERVER_ERROR);
      ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
      response
          .getWriter()
          .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(CommonConstant.AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(CommonConstant.BEARER)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
