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

                Map<String, Object> result =
                    this.loadTestThread(databaseUrl, jdbcDriverClass, username, password, sql, j);
                JdbcDataResponse jdbcDataResponse = this.buildJdbcDataResponse(result);
                sseEmitter.send(jdbcDataResponse, MediaType.APPLICATION_JSON);
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
            }).start();

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
    long startTime = System.currentTimeMillis();
    executorService.execute(
          () -> {
            try {
              long timeElapsed = 0;
              do {

                Map<String, Object> result =
                    this.loadTestThread(databaseUrl, jdbcDriverClass, username, password, sql, 0);
                JdbcDataResponse jdbcDataResponse = this.buildJdbcDataResponse(result);
                sseEmitter.send(jdbcDataResponse, MediaType.APPLICATION_JSON);
                long endTime = System.currentTimeMillis();
                timeElapsed = endTime - startTime;
              } while (timeElapsed < durationTime * 1000L);
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            } finally {
              latch.countDown();
            }
          });
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
            }).start();

    return sseEmitter;
  }
  private JdbcDataResponse buildJdbcDataResponse(
      Map<String, Object> result) {
    return jdbcRequestMapper.toJdbcDataResponse(
        result.get(CommonConstant.THREAD_NAME),
        result.get(CommonConstant.ITERATIONS),
        result.get(CommonConstant.START_AT),
        result.get(CommonConstant.NAME_DBMS),
        result.get(CommonConstant.VERSION_DBMS),
        result.get(CommonConstant.ERROR_CODE),
        result.get(CommonConstant.ERROR_MESSAGE),
        result.get(CommonConstant.CONTENT_TYPE),
        result.get(CommonConstant.LOAD_TIME),
        result.get(CommonConstant.CONNECT_TIME),
        result.get(CommonConstant.LATENCY),
        result.get(CommonConstant.DATA_SENT),
        result.get(CommonConstant.DATA_RECEIVED),
        result.get(CommonConstant.DATA));
  }

  public Map<String, Object> loadTestThread(
      String databaseUrl,
      String jdbcDriverClass,
      String username,
      String password,
      String sql,
      int iterations)
      throws ClassNotFoundException, JsonProcessingException {
    Map<String, Object> result = new HashMap<>();
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

      //Get name database management system
      DatabaseMetaData dbmd = connection.getMetaData();
      result.put(CommonConstant.NAME_DBMS, dbmd.getDatabaseProductName());
      result.put(CommonConstant.VERSION_DBMS, dbmd.getDatabaseProductVersion());


      //Get data
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      int cols = resultSetMetaData.getColumnCount();
      List<JsonNode> jsonNodeList = new ArrayList<>();
      JsonNode jsonNode = null;
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


      
      //Calc data received
      Statement statement1 = connection.createStatement();
      ResultSet resultSet1 = statement1.executeQuery(sql);
      long responseTime = System.currentTimeMillis();

      long bodySize = 0;
      while (resultSet1.next()) {
        StringBuilder rowBuilder = new StringBuilder();
        for (int i = 1; i <= resultSet1.getMetaData().getColumnCount(); i++) {
          rowBuilder.append(resultSet1.getString(i));
        }
        bodySize += rowBuilder.toString().getBytes().length;
      }

      //calc time
      long loadEndTime = System.currentTimeMillis();
      long latency = responseTime - loadStartTime;
      long loadTime = loadEndTime - loadStartTime;
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.DATA_SENT,"0");
      result.put(
          CommonConstant.DATA_RECEIVED,
          String.valueOf(bodySize));
      result.put(CommonConstant.ERROR_CODE,"1");
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.CONTENT_TYPE,"text");

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

}
