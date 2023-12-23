package com.pbl.loadtestweb.httprequest.service.impl;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.httprequest.mapper.HttpRequestMapper;
import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import com.pbl.loadtestweb.httprequest.utils.Utils;
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
  public SseEmitter httpGet(String url, int threadCount, int iterations) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.sendHttpRequest(url, CommonConstant.HTTP_METHOD_GET, j);
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
  public SseEmitter httpGetWithRampUp(String url, int threadCount, int iterations, int rampUp) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    log.info(Long.toString(Utils.threadRunEachMillisecond(threadCount, rampUp)));
    log.info(Long.toString(Utils.calcThreadIncrement(threadCount, rampUp)));

    for (int i = 1; i <= threadCount; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.threadRunEachMillisecond(threadCount, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.sendHttpRequest(url, CommonConstant.HTTP_METHOD_GET, j);
                HttpDataResponse jsonResponse = this.buildHttpDataResponse(result);
                sseEmitter.send(jsonResponse, MediaType.APPLICATION_JSON);
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
      String url, int threadCount, int iterations, HttpPostRequest httpPostRequest) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result =
                    this.sendHttpRequestWithFormURLEncoded(
                        url, CommonConstant.HTTP_METHOD_POST, httpPostRequest, j);
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
      String url, int threadCount, int iterations, HttpPostRequest httpPostRequest) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result =
                    this.sendHttpRequestWithJson(
                        url, CommonConstant.HTTP_METHOD_POST, httpPostRequest, j);
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

  public Map<String, String> sendHttpRequest(String url, String method, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setRequestMethod(method);

      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));

      long startTime = System.currentTimeMillis();
      connection.connect();
      long connectTime = System.currentTimeMillis() - startTime;

      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream inputStream = connection.getInputStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }

        String responseBody = Utils.getResponseBodySuccess(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        long htmlTransferred = responseBody.getBytes().length;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.HEADER_SIZE, String.valueOf(Utils.calcHeaderSize(connection)));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
        result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
        result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
        result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
        result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
        result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
        result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());
      } else {
        InputStream inputStream = connection.getErrorStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }
        String responseBody = Utils.getResponseBodyError(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        long htmlTransferred = responseBody.getBytes().length;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.HEADER_SIZE, String.valueOf(Utils.calcHeaderSize(connection)));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
        result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
        result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
        result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
        result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
        result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
        result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());
      }

    } catch (IOException e) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_MESSAGE, e.getMessage());
      log.error(e.getMessage());
    }
    return result;
  }

  public Map<String, String> sendHttpRequestWithFormURLEncoded(
      String url, String method, HttpPostRequest httpPostRequest, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      long startTime = System.currentTimeMillis();
      connection.connect();
      long connectTime = System.currentTimeMillis() - startTime;

      String requestBody = Utils.handleParamsToRequestBodyMVC(httpPostRequest);

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(requestBody);
      }

      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));

      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream inputStream = connection.getInputStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }

        String responseBody = Utils.getResponseBodySuccess(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        long htmlTransferred = responseBody.getBytes().length;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.HEADER_SIZE, String.valueOf(Utils.calcHeaderSize(connection)));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
        result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
        result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
        result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
        result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
        result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
        result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());
      } else {
        InputStream inputStream = connection.getErrorStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }
        String responseBody = Utils.getResponseBodyError(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        long htmlTransferred = responseBody.getBytes().length;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.HEADER_SIZE, String.valueOf(Utils.calcHeaderSize(connection)));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
        result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
        result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
        result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
        result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
        result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
        result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());
      }

    } catch (Exception e) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_MESSAGE, e.getMessage());
      log.error(e.getMessage());
    }
    return result;
  }

  public Map<String, String> sendHttpRequestWithJson(
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
      String params = Utils.handleParamsToRequestBodyAPI(httpPostRequest);
      connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));

      long startTime = System.currentTimeMillis();
      connection.connect();
      long connectTime = System.currentTimeMillis() - startTime;

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(params);
      }

      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));

      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream inputStream = connection.getInputStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }

        String responseBody = Utils.getResponseBodySuccess(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        long htmlTransferred = responseBody.getBytes().length;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.HEADER_SIZE, String.valueOf(Utils.calcHeaderSize(connection)));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
        result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
        result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
        result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
        result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
        result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
        result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());
      } else {
        InputStream inputStream = connection.getErrorStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }
        String responseBody = Utils.getResponseBodyError(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        long htmlTransferred = responseBody.getBytes().length;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.HEADER_SIZE, String.valueOf(Utils.calcHeaderSize(connection)));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.HTML_TRANSFERRED, String.valueOf(htmlTransferred));
        result.put(CommonConstant.KEEP_ALIVE, String.valueOf(isKeepAlive));
        result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
        result.put(CommonConstant.RESPONSE_CODE, Integer.toString(connection.getResponseCode()));
        result.put(CommonConstant.RESPONSE_MESSAGE, connection.getResponseMessage());
        result.put(CommonConstant.CONTENT_TYPE, connection.getContentType());
        result.put(CommonConstant.DATA_ENCODING, connection.getContentEncoding());
        result.put(CommonConstant.REQUEST_METHOD, connection.getRequestMethod());
      }

    } catch (Exception e) {
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
      result.put(CommonConstant.ITERATIONS, Integer.toString(iterations));
      result.put(CommonConstant.RESPONSE_MESSAGE, e.getMessage());
      log.error(e.getMessage());
    }
    return result;
  }
}
