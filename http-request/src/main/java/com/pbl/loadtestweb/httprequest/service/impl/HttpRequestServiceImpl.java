package com.pbl.loadtestweb.httprequest.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.httprequest.mapper.HttpRequestMapper;
import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpRequestServiceImpl implements HttpRequestService {

  private final HttpRequestMapper httpRequestMapper;

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  @Override
  public SseEmitter httpGet(String url, int threadCount, int iterations, String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.loadTestThread(url, method, j);
                HttpDataResponse jsonResponse = this.buildHttpDataResponse(result);
                sseEmitter.send(jsonResponse, MediaType.APPLICATION_JSON);
                sleep();
              }
            } catch (IOException e) {
              e.printStackTrace();
              sseEmitter.completeWithError(e);
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
  public SseEmitter httpPostMVC(
      String url, int threadCount, int iterations, String method, HttpPostRequest httpPostRequest) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.loadTestThreadWithParamsMVC(url, method, httpPostRequest, j);
                HttpDataResponse jsonResponse = this.buildHttpDataResponse(result);
                sseEmitter.send(jsonResponse, MediaType.APPLICATION_JSON);
                sleep();
              }
            } catch (IOException e) {
              e.printStackTrace();
              sseEmitter.completeWithError(e);
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
  public SseEmitter httpPostAPI(
      String url, int threadCount, int iterations, String method, HttpPostRequest httpPostRequest) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.loadTestThreadWithParamsAPI(url, method, httpPostRequest, j);
                HttpDataResponse jsonResponse = this.buildHttpDataResponse(result);
                sseEmitter.send(jsonResponse, MediaType.APPLICATION_JSON);
                sleep();
              }
            } catch (IOException e) {
              e.printStackTrace();
              sseEmitter.completeWithError(e);
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

  private HttpDataResponse buildHttpDataResponse(Map<String, String> result) {
    return httpRequestMapper.toHttpDataResponse(
        result.get(CommonConstant.SERVER_SOFTWARE),
        result.get(CommonConstant.SERVER_HOST),
        result.get(CommonConstant.SERVER_PORT),
        result.get(CommonConstant.THREAD_NAME),
        result.get(CommonConstant.ITERATIONS),
        result.get(CommonConstant.START_AT),
        result.get(CommonConstant.RESPONSE_CODE),
        result.get(CommonConstant.RESPONSE_MESSAGE),
        result.get(CommonConstant.RESPONSE_BODY),
        result.get(CommonConstant.CONTENT_TYPE),
        result.get(CommonConstant.DATA_ENCODING),
        result.get(CommonConstant.REQUEST_METHOD),
        result.get(CommonConstant.LOAD_TIME),
        result.get(CommonConstant.CONNECT_TIME),
        result.get(CommonConstant.LATENCY),
        result.get(CommonConstant.HEADER_SIZE),
        result.get(CommonConstant.HTML_TRANSFERRED),
        result.get(CommonConstant.KEEP_ALIVE));
  }

  private long calcHeaderSize(HttpURLConnection connection) {
    Map<String, List<String>> responseHeaders = connection.getHeaderFields();
    long headersSize = 0;

    for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
      String headerName = entry.getKey();
      List<String> headerValues = entry.getValue();

      for (String headerValue : headerValues) {
        headersSize += (headerName + ": " + headerValue + "\r\n").getBytes().length;
      }
    }
    return headersSize;
  }

  private String getResponseBody(HttpURLConnection connection) {
    BufferedReader br = null;
    StringBuilder body = null;
    String line = "";
    try {
      br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      body = new StringBuilder();
      while ((line = br.readLine()) != null) {
        body.append(line);
      }
      return body.toString();
    } catch (Exception e) {
      throw new RuntimeException("Unable to get response body", e);
    }
  }

  private boolean isKeepAlive(HttpURLConnection connection) {
    String connectionHeader = connection.getHeaderField("Connection");
    return "keep-alive".equalsIgnoreCase(connectionHeader);
  }

  public Map<String, String> loadTestThread(String url, String method, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

      long loadStartTime = System.currentTimeMillis();
      long connectStartTime = System.currentTimeMillis();

      connection.connect();

      long connectEndTime = System.currentTimeMillis();

      InputStream inputStream = connection.getInputStream();
      long responseTime = 0;

      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }
      String responseBody = this.getResponseBody(connection);
      long loadEndTime = System.currentTimeMillis();
      result.put(CommonConstant.RESPONSE_BODY, responseBody);
      long htmlTransferred = responseBody.getBytes().length;
      boolean isKeepAlive = this.isKeepAlive(connection);

      long latency = responseTime - connectStartTime;
      long connectTime = connectEndTime - connectStartTime;
      long loadTime = loadEndTime - loadStartTime;

      result.put(CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
      result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
      result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(this.calcHeaderSize(connection)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
      result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
      result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
      result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
      result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
      result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());

    } catch (Exception ignored) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_MESSAGE, ignored.getMessage());
      log.error(ignored.getMessage());
    }
    return result;
  }

  private String handleParamsToRequestBodyMVC(HttpPostRequest httpPostRequest) {
    Map<String, String> params = new HashMap<>();
    for (int i = 0; i < httpPostRequest.getKey().size(); i++) {
      params.put(httpPostRequest.getKey().get(i), httpPostRequest.getValue().get(i));
    }
    StringBuilder requestBody = new StringBuilder();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (requestBody.length() > 0) {
        requestBody.append("&");
      }
      requestBody.append(entry.getKey()).append("=").append(entry.getValue());
    }
    return requestBody.toString();
  }

  public String handleParamsToRequestBodyAPI(HttpPostRequest httpPostRequest) throws Exception {
    Map<String, String> params = new HashMap<>();
    for (int i = 0; i < httpPostRequest.getKey().size(); i++) {
      params.put(httpPostRequest.getKey().get(i), httpPostRequest.getValue().get(i));
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(params);
  }

  public Map<String, String> loadTestThreadWithParamsMVC(
      String url, String method, HttpPostRequest httpPostRequest, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

      long loadStartTime = System.currentTimeMillis();
      long connectStartTime = System.currentTimeMillis();

      connection.setDoInput(true);
      connection.setDoOutput(true);

      connection.connect();
      long connectEndTime = System.currentTimeMillis();

      String requestBody = this.handleParamsToRequestBodyMVC(httpPostRequest);

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(requestBody);
      }

      InputStream inputStream = connection.getInputStream();
      long responseTime = 0;
      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }

      String responseBody = this.getResponseBody(connection);
      long loadEndTime = System.currentTimeMillis();
      result.put(CommonConstant.RESPONSE_BODY, responseBody);
      long htmlTransferred = responseBody.getBytes().length;
      boolean isKeepAlive = this.isKeepAlive(connection);

      long latency = responseTime - connectStartTime;
      long connectTime = connectEndTime - connectStartTime;
      long loadTime = loadEndTime - loadStartTime;

      result.put(CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
      result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
      result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(this.calcHeaderSize(connection)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
      result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
      result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
      result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
      result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
      result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());

    } catch (Exception ignored) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_MESSAGE, ignored.getMessage());
      log.error(ignored.getMessage());
    }
    return result;
  }

  public Map<String, String> loadTestThreadWithParamsAPI(
      String url, String method, HttpPostRequest httpPostRequest, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");

      log.info(this.handleParamsToRequestBodyAPI(httpPostRequest));
      String params = this.handleParamsToRequestBodyAPI(httpPostRequest);

      connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(params);
      }

      long loadStartTime = System.currentTimeMillis();
      long connectStartTime = System.currentTimeMillis();
      connection.connect();
      long connectEndTime = System.currentTimeMillis();

      InputStream inputStream = connection.getInputStream();
      long responseTime = 0;
      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }

      String responseBody = this.getResponseBody(connection);
      long loadEndTime = System.currentTimeMillis();
      result.put(CommonConstant.RESPONSE_BODY, responseBody);
      long htmlTransferred = responseBody.getBytes().length;
      boolean isKeepAlive = this.isKeepAlive(connection);


      long latency = responseTime - connectStartTime;
      long connectTime = connectEndTime - connectStartTime;
      long loadTime = loadEndTime - loadStartTime;

      result.put(CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
      result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
      result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(this.calcHeaderSize(connection)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
      result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
      result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
      result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
      result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
      result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());

    } catch (Exception ignored) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_MESSAGE, ignored.getMessage());
      log.error(ignored.getMessage());
    }
    return result;
  }
}
