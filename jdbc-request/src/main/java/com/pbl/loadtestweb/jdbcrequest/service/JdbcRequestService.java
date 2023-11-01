package com.pbl.loadtestweb.jdbcrequest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface JdbcRequestService {
  JdbcDataResponse handleJdbcRequest(
      String databaseUrl, String jdbcDriverClass, String username, String password, String sql)
      throws ClassNotFoundException;

  List<JsonNode> handleJdbcData(
      String databaseUrl, String jdbcDriverClass, String username, String password, String sql)
      throws ClassNotFoundException, SQLException, JsonProcessingException;
}
