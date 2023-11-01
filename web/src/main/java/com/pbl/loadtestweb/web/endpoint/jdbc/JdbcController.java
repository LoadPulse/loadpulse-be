package com.pbl.loadtestweb.web.endpoint.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import com.pbl.loadtestweb.jdbcrequest.service.JdbcRequestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.devtools.v116.network.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
      @RequestParam(name = "password", defaultValue = " ") String password,
      @RequestParam(name = "sqlStatemment", defaultValue = " ") String sql)
      throws ClassNotFoundException {
    return ResponseEntity.ok(
        jdbcRequestService.handleJdbcRequest(
            databaseUrl, jdbcDriverClass, username, password, sql));
  }

  @GetMapping(value = "/column")
  public ResponseEntity<List<JsonNode>> getJdbcData(
      @RequestParam(name = "databaseUrl", defaultValue = " ") String databaseUrl,
      @RequestParam(name = "jdbcDriverClass", defaultValue = " ") String jdbcDriverClass,
      @RequestParam(name = "username", defaultValue = " ") String username,
      @RequestParam(name = "password", defaultValue = " ") String password,
      @RequestParam(name = "sqlStatement", defaultValue = " ") String sql)
      throws SQLException, ClassNotFoundException, JsonProcessingException {
    return ResponseEntity.ok(
        jdbcRequestService.handleJdbcData(databaseUrl, jdbcDriverClass, username, password, sql));
  }
}
