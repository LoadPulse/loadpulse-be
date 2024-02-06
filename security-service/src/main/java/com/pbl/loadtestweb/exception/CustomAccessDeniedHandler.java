package com.pbl.loadtestweb.exception;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.MessageConstant;
import com.pbl.loadtestweb.common.payload.general.ResponseDataAPI;
import com.pbl.loadtestweb.common.payload.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = CommonFunction.getExceptionError(MessageConstant.FORBIDDEN);
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(errorResponse);
    log.error(request.getMethod(), request.getRequestURI(), accessDeniedException.getMessage());

    response
        .getWriter()
        .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
  }
}
