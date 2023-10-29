package com.pbl.loadtestweb.jdbcrequest.service;

import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;

import java.util.List;

public interface JdbcRequestService {
  List<JdbcDataResponse> handleJdbcRequest(
      String databaseUrl, String jdbcDriverClass, String username, String password)
      throws ClassNotFoundException;
}
