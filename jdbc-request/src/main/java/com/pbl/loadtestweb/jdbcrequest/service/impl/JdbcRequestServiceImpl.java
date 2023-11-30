package com.pbl.loadtestweb.jdbcrequest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.jdbcrequest.mapper.JdbcRequestMapper;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import com.pbl.loadtestweb.jdbcrequest.service.JdbcRequestService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcRequestServiceImpl implements JdbcRequestService {
  private final JdbcRequestMapper jdbcRequestMapper;

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  @Override
  public SseEmitter jdbcLoadTestWeb(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int threadCount,
      int iterations) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);
    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {

                Map<String, String> result =
                    this.loadTestThread(databaseUrl, jdbcDriverClass, username, password, sql, j);
                JdbcDataResponse jdbcDataResponse = this.buildJdbcDataResponse(result);
                sseEmitter.send(jdbcDataResponse, MediaType.APPLICATION_JSON);
                sleep();
              }
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            } finally {
              latch.countDown();
            }
          });
    }

    new Thread(
            () -> {
              try {
                latch.await();
                sseEmitter.complete();
              } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                sseEmitter.completeWithError(e);
              }
            })
        .start();

    return sseEmitter;
  }

  public SseEmitter jdbcDataLoadTestWeb(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int threadCount,
      int iterations) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);
    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                List<JsonNode> result =
                    this.handleJdbcData(databaseUrl, jdbcDriverClass, username, password, sql);
                sseEmitter.send(result, MediaType.APPLICATION_JSON);
                sleep();
              }
            } catch (ClassNotFoundException | IOException | SQLException e) {
              throw new RuntimeException(e);
            } finally {
              latch.countDown();
            }
          });
    }

    new Thread(
            () -> {
              try {
                latch.await();
                sseEmitter.complete();
              } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                sseEmitter.completeWithError(e);
              }
            })
        .start();

    return sseEmitter;
  }

  private void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  public List<JsonNode> handleJdbcData(
      String databaseUrl, String jdbcDriverClass, String username, String password, String sql)
      throws ClassNotFoundException, SQLException, JsonProcessingException {
    return this.getJdbcData(databaseUrl, jdbcDriverClass, username, password, sql);
  }

  private JdbcDataResponse buildJdbcDataResponse(Map<String, String> result) {
    return jdbcRequestMapper.toJdbcDataResponse(
        result.get(CommonConstant.THREAD_NAME),
        result.get(CommonConstant.ITERATIONS),
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

  private long calcBodySize(
      String databaseUrl, String jdbcDriverClass, String username, String password, String sql)
      throws ClassNotFoundException, SQLException {
    Class.forName(jdbcDriverClass);
    Connection connection = DriverManager.getConnection(databaseUrl, username, password);
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(sql);
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
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int iterations)
      throws ClassNotFoundException {
    Map<String, String> result = new HashMap<>();
    try {
      long startConnectTime = System.currentTimeMillis();
      Class.forName(jdbcDriverClass);

      Connection connection = DriverManager.getConnection(databaseUrl, username, password);
      long endConnectTime = System.currentTimeMillis();
      long loadStartTime = System.currentTimeMillis();
      long connectTime = endConnectTime - startConnectTime;

      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);

      InputStream inputStream = connection.createNClob().getAsciiStream();
      long responseTime = 0;
      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }

      long loadEndTime = System.currentTimeMillis();

      long latency = responseTime - endConnectTime;
      long loadTime = loadEndTime - loadStartTime;
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(0));
      result.put(
          CommonConstant.BODY_SIZE,
          String.valueOf(this.calcBodySize(databaseUrl, jdbcDriverClass, username, password, sql)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_CODE, "200");

      result.put(CommonConstant.RESPONSE_MESSAGE, "OK");
    } catch (SQLException ignored) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.RESPONSE_CODE, String.valueOf(ignored.getErrorCode()));
      result.put(CommonConstant.RESPONSE_MESSAGE, ignored.getMessage());
    }
    return result;
  }

  public List<JsonNode> getJdbcData(
      String databaseUrl, String jdbcDriverClass, String username, String password, String sql)
      throws JsonProcessingException {
    List<JsonNode> jsonNodeList = new ArrayList<>();
    JsonNode jsonNode = null;
    try {
      Class.forName(jdbcDriverClass);
      Connection connection = DriverManager.getConnection(databaseUrl, username, password);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      int cols = resultSetMetaData.getColumnCount();

      while (resultSet.next()) {
        JsonObject jsonObject = new JsonObject();
        for (int ii = 1; ii <= cols; ii++) {
          String alias = resultSetMetaData.getColumnLabel(ii);
          jsonObject.addProperty(alias, resultSet.getString(alias));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonObject.toString());
        jsonNodeList.add(jsonNode);
      }
    } catch (SQLException | ClassNotFoundException ignored) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty(CommonConstant.RESPONSE_MESSAGE, ignored.getMessage());
      ObjectMapper objectMapper = new ObjectMapper();
      jsonNode = objectMapper.readTree(jsonObject.toString());
      jsonNodeList.add(jsonNode);
    }
    return jsonNodeList;
  }
}
