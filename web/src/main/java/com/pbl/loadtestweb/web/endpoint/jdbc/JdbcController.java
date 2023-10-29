package com.pbl.loadtestweb.web.endpoint.jdbc;

import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import com.pbl.loadtestweb.jdbcrequest.service.JdbcRequestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.devtools.v116.network.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/jdbc-request")
@Api(tags = "Jdbc Request APIs")
public class JdbcController {
  private final JdbcRequestService jdbcRequestService;

  @GetMapping(value = "/")
  public ResponseEntity<JdbcDataResponse> handleJdbcResponse(
      @RequestParam(name = "databaseUrl", defaultValue = " ") String databaseUrl,
      @RequestParam(name = "jdbcDriverClass", defaultValue = " ") String jdbcDriverClass,
      @RequestParam(name = "username", defaultValue = " ") String username,
      @RequestParam(name = "password", defaultValue = " ") String password)
      throws ClassNotFoundException {
    return ResponseEntity.ok(
        jdbcRequestService.handleJdbcRequest(databaseUrl, jdbcDriverClass, username, password));
  }

  @GetMapping(value = "/column")
  public ResponseEntity<JdbcDataResponse> handleColumnJdbc(
      @RequestParam(name = "databaseUrl", defaultValue = " ") String databaseUrl,
      @RequestParam(name = "jdbcDriverClass", defaultValue = " ") String jdbcDriverClass,
      @RequestParam(name = "username", defaultValue = " ") String username,
      @RequestParam(name = "password", defaultValue = " ") String password)
      throws SQLException, ClassNotFoundException {
    return  ResponseEntity.ok(
            jdbcRequestService.handleJdbcColumn(databaseUrl, jdbcDriverClass, username, password));
  }
}
