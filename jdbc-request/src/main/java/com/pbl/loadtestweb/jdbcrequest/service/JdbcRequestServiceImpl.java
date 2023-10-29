package com.pbl.loadtestweb.jdbcrequest.service;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.jdbcrequest.mapper.JdbcRequestMapper;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcRequestServiceImpl implements JdbcRequestService {
  private final JdbcRequestMapper jdbcRequestMapper;

  @Override
  public JdbcDataResponse handleJdbcRequest(
      String databaseUrl, String jdbcDriverClass, String username, String password)
      throws ClassNotFoundException {
    Map<String, String> result =
        this.loadTestThread(databaseUrl, jdbcDriverClass, username, password);
    return buildJdbcDataResponse(result);
  }
  @Override
  public  JdbcDataResponse handleJdbcColumn(String databaseUrl, String jdbcDriverClass, String username, String password) throws ClassNotFoundException, SQLException {
    Map<String,List<String>> result = this.getColumnName(databaseUrl, jdbcDriverClass, username, password);
    return bulidJdbcDataColumnResponse(result);
  }
  private JdbcDataResponse buildJdbcDataResponse(Map<String, String> result) {
    return jdbcRequestMapper.toJdbcDataResponse(
        result.get(CommonConstant.THREAD_NAME),
        // result.get(CommonConstant.ITERATIONS),
        result.get(CommonConstant.START_AT),
        result.get(CommonConstant.RESPONSE_CODE),
        result.get(CommonConstant.RESPONSE_MESSAGE),
        result.get(CommonConstant.CONTENT_TYPE),
        result.get(CommonConstant.DATA_ENCODING),
        // result.get(CommonConstant.REQUEST_METHOD),
        result.get(CommonConstant.LOAD_TIME),
        result.get(CommonConstant.CONNECT_TIME),
        result.get(CommonConstant.LATENCY),
        result.get(CommonConstant.HEADER_SIZE),
        result.get(CommonConstant.BODY_SIZE));
  }
  public JdbcDataResponse bulidJdbcDataColumnResponse(Map<String,List<String>> result)
  {
    return jdbcRequestMapper.toJdbcDataColumnResponse(
            result.get(CommonConstant.COLUMN_NAME)
    );
  }
  public ResultSet connectMysqlDb(
      String databaseUrl, String jdbcDriverClass, String username, String password)
      throws ClassNotFoundException, SQLException {
    Class.forName(jdbcDriverClass);
    Connection connection = DriverManager.getConnection(databaseUrl, username, password);
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM wordpress.wp_users");
    return resultSet;
  }
  private long calcBodySize(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password)
      throws ClassNotFoundException, SQLException {
    ResultSet resultSet = connectMysqlDb(databaseUrl, jdbcDriverClass, username, password);
    long bodySize = 0;
    while (resultSet.next()) {
      StringBuilder rowBuilder = new StringBuilder();
      for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
        rowBuilder.append(resultSet.getString(i));
      }
      bodySize += rowBuilder.toString().getBytes().length;
    }
    return bodySize;
  }

  public Map<String, String> loadTestThread(
      String databaseUrl, String jdbcDriverClass, String username, String password)
      throws ClassNotFoundException {
    Map<String, String> result = new HashMap<>();

    long startTime = System.currentTimeMillis();
    Class.forName(jdbcDriverClass);

    try {
      Connection connection = DriverManager.getConnection(databaseUrl, username, password);
      long connectTime = System.currentTimeMillis() - startTime;
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM wordpress.wp_users");

      long latency = 76;
      long loadTime = 77;
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(0));
      result.put(
          CommonConstant.BODY_SIZE,
          String.valueOf(
              this.calcBodySize(databaseUrl, jdbcDriverClass, username, password)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      // result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_CODE, "200");


      result.put(CommonConstant.RESPONSE_MESSAGE, " ");
    } catch (SQLException e) {

    }
    return result;
  }

  public Map<String, List<String>> getColumnName(
      String databaseUrl, String jdbcDriverClass, String username, String password)
      throws ClassNotFoundException, SQLException {
    ResultSet resultSet = connectMysqlDb(databaseUrl,jdbcDriverClass, username, password);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    int cols = resultSetMetaData.getColumnCount();
    Map<String, List<String>> result = new HashMap<>();
    List<String> columnNameList = new  ArrayList<>();

    for (int i = 1; i <= cols; i++) {
      columnNameList.add(resultSetMetaData.getColumnName(i));
    }
    result.put(CommonConstant.COLUMN_NAME, columnNameList);
    return result;
  }
}
