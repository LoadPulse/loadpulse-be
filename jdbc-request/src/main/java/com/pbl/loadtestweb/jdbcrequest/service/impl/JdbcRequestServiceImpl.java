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

  public static long threadRunEachMillisecond(int virtualUsers, int rampUp) {
    return (long) ((double) rampUp / virtualUsers * 1000);
  }

  public static void sleepThread(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public SseEmitter jdbcLoadTestWeb(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int virtualUsers,
      int iterations,
      int rampUp) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        sleepThread(threadRunEachMillisecond(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {

                Map<String, String> result =
                    this.loadTestThread(databaseUrl, jdbcDriverClass, username, password, sql, j);
                Map<String, List<JsonNode>> resultData =
                    this.getJdbcData(databaseUrl, jdbcDriverClass, username, password, sql);
                JdbcDataResponse jdbcDataResponse = this.buildJdbcDataResponse(result, resultData);
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
  @Override
  public SseEmitter jdbcLoadTestWebWithDuration(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int virtualUsers,
      int rampUp,
      long durationTime) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
    CountDownLatch latch = new CountDownLatch(virtualUsers);
    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        sleepThread(threadRunEachMillisecond(virtualUsers, rampUp));
      }
      long startTime = System.currentTimeMillis();
      executorService.execute(
              () -> {
                try {
                  long timeElapsed = 0;
                  do {

                    Map<String, String> result =
                            this.loadTestThread(databaseUrl, jdbcDriverClass, username, password, sql,0);
                    Map<String, List<JsonNode>> resultData =
                            this.getJdbcData(databaseUrl, jdbcDriverClass, username, password, sql);
                    JdbcDataResponse jdbcDataResponse = this.buildJdbcDataResponse(result, resultData);
                    sseEmitter.send(jdbcDataResponse, MediaType.APPLICATION_JSON);
                    sleep();
                    long endTime = System.currentTimeMillis();
                    timeElapsed = endTime - startTime;
                  } while (timeElapsed < durationTime * 1000L);
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

  private void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  private JdbcDataResponse buildJdbcDataResponse(
      Map<String, String> result, Map<String, List<JsonNode>> resultdata) {
    return jdbcRequestMapper.toJdbcDataResponse(
        result.get(CommonConstant.THREAD_NAME),
        result.get(CommonConstant.ITERATIONS),
        result.get(CommonConstant.START_AT),
        result.get(CommonConstant.NAME_DBMS),
        result.get(CommonConstant.VERSION_DBMS),
        result.get(CommonConstant.ERROR_CODE),
        result.get(CommonConstant.ERROR_MESSAGE),
        result.get(CommonConstant.CONTENT_TYPE),
        result.get(CommonConstant.DATA_ENCODING),
        result.get(CommonConstant.LOAD_TIME),
        result.get(CommonConstant.CONNECT_TIME),
        result.get(CommonConstant.LATENCY),
        result.get(CommonConstant.BODY_SIZE),
        resultdata.get(CommonConstant.DATA));
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
    long loadStartTime = System.currentTimeMillis();
    try {
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      Class.forName(jdbcDriverClass);
      long startConnectTime = System.currentTimeMillis();
      Connection connection = DriverManager.getConnection(databaseUrl, username, password);
      if (iterations != 0) {
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      }
      long endConnectTime = System.currentTimeMillis();

      long connectTime = endConnectTime - startConnectTime;

      DatabaseMetaData dbmd = connection.getMetaData();
      result.put(CommonConstant.NAME_DBMS, dbmd.getDatabaseProductName());
      result.put(CommonConstant.VERSION_DBMS, dbmd.getDatabaseProductVersion());

      long responseTime = System.currentTimeMillis();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);

      long loadEndTime = System.currentTimeMillis();

      long latency = responseTime - loadStartTime;
      long loadTime = loadEndTime - loadStartTime;
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(
          CommonConstant.BODY_SIZE,
          String.valueOf(this.calcBodySize(databaseUrl, jdbcDriverClass, username, password, sql)));

      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
    } catch (SQLException ignored) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.ERROR_CODE, String.valueOf(ignored.getErrorCode()));
      result.put(CommonConstant.ERROR_MESSAGE, ignored.getMessage());
    }
    return result;
  }

  public Map<String, List<JsonNode>> getJdbcData(
      String databaseUrl, String jdbcDriverClass, String username, String password, String sql)
      throws JsonProcessingException {
    List<JsonNode> jsonNodeList = new ArrayList<>();
    Map<String, List<JsonNode>> result = new HashMap<>();
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
      result.put(CommonConstant.DATA, jsonNodeList);
    } catch (SQLException | ClassNotFoundException ignored) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty(CommonConstant.ERROR_MESSAGE, ignored.getMessage());
      ObjectMapper objectMapper = new ObjectMapper();
      jsonNode = objectMapper.readTree(jsonObject.toString());
      jsonNodeList.add(jsonNode);
    }
    return result;
  }
}
