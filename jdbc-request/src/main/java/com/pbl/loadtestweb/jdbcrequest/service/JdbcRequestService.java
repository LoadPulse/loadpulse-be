package com.pbl.loadtestweb.jdbcrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface JdbcRequestService {
  SseEmitter jdbcLoadTestWeb(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int threadCount,
      int iterations,
      int ramUp);
  SseEmitter jdbcLoadTestWebWithDuration(
          String databaseUrl,
          String jdbcDriverClass,
          String username,
          String password,
          String sql,
          int threadCount,
          int iterations,
          int rampUp, long durationTime);
}
