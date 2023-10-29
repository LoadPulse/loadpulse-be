package com.pbl.loadtestweb.jdbcrequest.service;

import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;

import java.sql.SQLException;

public interface JdbcRequestService {
  JdbcDataResponse handleJdbcRequest(
      String databaseUrl, String jdbcDriverClass, String username, String password)
      throws ClassNotFoundException;
  JdbcDataResponse handleJdbcColumn(String databaseUrl, String jdbcDriverClass, String username, String password)
    throws ClassNotFoundException, SQLException;
}
