package com.pbl.loadpulse.web.endpoint.jdbc;

import com.pbl.loadpulse.jdbcrequest.service.JdbcRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/jdbc-request")
@Tag(name = "Jdbc Request APIs")
public class JdbcController {
  private final JdbcRequestService jdbcRequestService;

  @GetMapping(value = "/")
  public ResponseEntity<SseEmitter> handleJdbcResponseDuration(
      @RequestParam(name = "database_url", defaultValue = " ") String databaseUrl,
      @RequestParam(name = "jdbc_driver_class", defaultValue = " ") String jdbcDriverClass,
      @RequestParam(name = "username", defaultValue = " ") String username,
      @RequestParam(name = "password", defaultValue = " ") String password,
      @RequestParam(name = "sql_statement", defaultValue = " ") String sql,
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "0") int iterations,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "duration", defaultValue = "0") long duration) {
    if (iterations == 0 && duration != 0) {
      return ResponseEntity.ok(
          jdbcRequestService.jdbcLoadTestWebWithDuration(
              databaseUrl,
              jdbcDriverClass,
              username,
              password,
              sql,
              virtualUsers,
              rampUp,
              duration));

    } else if (iterations != 0 && duration == 0) {
      return ResponseEntity.ok(
          jdbcRequestService.jdbcLoadTestWeb(
              databaseUrl,
              jdbcDriverClass,
              username,
              password,
              sql,
              virtualUsers,
              iterations,
              rampUp));
    }
    return null;
  }
}
