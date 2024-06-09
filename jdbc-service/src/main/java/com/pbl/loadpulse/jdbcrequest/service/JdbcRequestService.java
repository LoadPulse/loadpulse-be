package com.pbl.loadpulse.jdbcrequest.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface JdbcRequestService {
  SseEmitter jdbcLoadTestWeb(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int virtualUsers,
      int iterations,
      int ramUp);

  SseEmitter jdbcLoadTestWebWithDuration(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int virtualUsers,
      int rampUp,
      long durationTime);

}
