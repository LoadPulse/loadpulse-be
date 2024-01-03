package com.pbl.loadtestweb.httprequest.service.impl;

import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.httprequest.mapper.HttpRequestMapper;
import com.pbl.loadtestweb.httprequest.payload.request.HttpRequest;
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
  public SseEmitter sendHttpRequest(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      HttpRequest httpRequest,
      String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    log.info(Long.toString(Utils.timeForCreationEachThread(virtualUsers, rampUp)));

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.timeForCreationEachThread(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.sendHttpRequest(url, method.toUpperCase(), j, httpRequest);
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
  public SseEmitter sendHttpRequestWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      HttpRequest httpRequest,
      String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.timeForCreationEachThread(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              long endTime = System.currentTimeMillis() + (durations * 1000L);

              while (System.currentTimeMillis() < endTime) {
                Map<String, String> result;
                result = this.sendHttpRequest(url, method.toUpperCase(), 1, httpRequest);
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
  public SseEmitter sendHttpRequestEncodedFormBody(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      HttpRequest httpRequest,
      String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.timeForCreationEachThread(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result =
                    this.sendHttpRequestWithFormURLEncoded(
                        url, method.toUpperCase(), httpRequest, j);
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
  public SseEmitter sendHttpRequestEncodedFormBodyWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      HttpRequest httpRequest,
      String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.timeForCreationEachThread(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              long endTime = System.currentTimeMillis() + (durations * 1000L);

              while (System.currentTimeMillis() < endTime) {
                Map<String, String> result;
                result =
                    this.sendHttpRequestWithFormURLEncoded(
                        url, method.toUpperCase(), httpRequest, 0);
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
  public SseEmitter sendHttpRequestJsonBody(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      HttpRequest httpRequest,
      String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.timeForCreationEachThread(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              for (int j = 1; j <= iterations; j++) {
                Map<String, String> result;
                result = this.sendHttpRequestWithJson(url, method.toUpperCase(), httpRequest, j);
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
  public SseEmitter sendHttpRequestJsonBodyWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      HttpRequest httpRequest,
      String method) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    CountDownLatch latch = new CountDownLatch(virtualUsers);

    log.info(Long.toString(Utils.timeForCreationEachThread(virtualUsers, rampUp)));

    for (int i = 1; i <= virtualUsers; i++) {
      if (i != 1) {
        Utils.sleepThread(Utils.timeForCreationEachThread(virtualUsers, rampUp));
      }
      executorService.execute(
          () -> {
            try {
              long endTime = System.currentTimeMillis() + (durations * 1000L);

              while (System.currentTimeMillis() < endTime) {
                Map<String, String> result;
                result = this.sendHttpRequestWithJson(url, method.toUpperCase(), httpRequest, 0);
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
        result.get(CommonConstant.RESPONSE_HEADER),
        result.get(CommonConstant.RESPONSE_BODY),
        result.get(CommonConstant.CONTENT_TYPE),
        result.get(CommonConstant.DATA_ENCODING),
        result.get(CommonConstant.REQUEST_METHOD),
        result.get(CommonConstant.LOAD_TIME),
        result.get(CommonConstant.CONNECT_TIME),
        result.get(CommonConstant.LATENCY),
        result.get(CommonConstant.DATA_RECEIVED),
        result.get(CommonConstant.DATA_SENT),
        result.get(CommonConstant.KEEP_ALIVE));
  }

  public Map<String, String> sendHttpRequest(
      String url, String method, int iterations, HttpRequest httpRequest) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setRequestMethod(method);

      for (int i = 0; i < httpRequest.getKeyHeaders().size(); i++) {
        connection.setRequestProperty(
            httpRequest.getKeyHeaders().get(i), httpRequest.getValueHeaders().get(i));
      }

      long dataSent = Utils.calcRequestHeaderSize(connection);

      result.put(CommonConstant.START_AT, String.valueOf(System.currentTimeMillis()));

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
        result.put(CommonConstant.RESPONSE_HEADER, Utils.getResponseHeaders(connection));
        long htmlTransferred = responseBody.getBytes().length;
        long dataReceived = Utils.calcResponseHeaderSize(connection) + htmlTransferred;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.DATA_RECEIVED, String.valueOf(dataReceived));
        result.put(CommonConstant.DATA_SENT, String.valueOf(dataSent));
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
        long dataReceived = Utils.calcResponseHeaderSize(connection) + htmlTransferred;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.DATA_RECEIVED, String.valueOf(dataReceived));
        result.put(CommonConstant.DATA_SENT, String.valueOf(dataSent));
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
      String url, String method, HttpRequest httpRequest, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      for (int i = 0; i < httpRequest.getKeyHeaders().size(); i++) {
        connection.setRequestProperty(
            httpRequest.getKeyHeaders().get(i), httpRequest.getValueHeaders().get(i));
      }

      long requestHeaderSize = Utils.calcRequestHeaderSize(connection);

      long startTime = System.currentTimeMillis();
      connection.connect();
      long connectTime = System.currentTimeMillis() - startTime;

      String requestBody = Utils.handleParamsToEncodedFormBody(httpRequest);
      long dataSent = requestBody.getBytes().length + requestHeaderSize;

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(requestBody);
      }

      result.put(CommonConstant.START_AT, String.valueOf(System.currentTimeMillis()));

      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream inputStream = connection.getInputStream();
        long latency = 0;
        if (inputStream != null) {
          latency = System.currentTimeMillis() - startTime;
        }

        String responseBody = Utils.getResponseBodySuccess(connection);
        long loadTime = System.currentTimeMillis() - startTime;
        result.put(CommonConstant.RESPONSE_BODY, responseBody);
        result.put(CommonConstant.RESPONSE_HEADER, Utils.getResponseHeaders(connection));
        long htmlTransferred = responseBody.getBytes().length;
        long dataReceived = Utils.calcResponseHeaderSize(connection) + htmlTransferred;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.DATA_RECEIVED, String.valueOf(dataReceived));
        result.put(CommonConstant.DATA_SENT, String.valueOf(dataSent));
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
        result.put(CommonConstant.RESPONSE_HEADER, Utils.getResponseHeaders(connection));
        long htmlTransferred = responseBody.getBytes().length;
        long dataReceived = Utils.calcResponseHeaderSize(connection) + htmlTransferred;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.DATA_RECEIVED, String.valueOf(dataReceived));
        result.put(CommonConstant.DATA_SENT, String.valueOf(dataSent));
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
      String url, String method, HttpRequest httpRequest, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");

      for (int i = 0; i < httpRequest.getKeyHeaders().size(); i++) {
        connection.setRequestProperty(
            httpRequest.getKeyHeaders().get(i), httpRequest.getValueHeaders().get(i));
      }

      String params = Utils.handleParamsToJsonBody(httpRequest);
      connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));

      long requestHeaderSize = Utils.calcRequestHeaderSize(connection);
      long dataSent = params.getBytes().length + requestHeaderSize;

      long startTime = System.currentTimeMillis();
      connection.connect();
      long connectTime = System.currentTimeMillis() - startTime;

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(params);
      }
      result.put(CommonConstant.START_AT, String.valueOf(System.currentTimeMillis()));

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
        long dataReceived = Utils.calcResponseHeaderSize(connection) + htmlTransferred;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.DATA_RECEIVED, String.valueOf(dataReceived));
        result.put(CommonConstant.DATA_SENT, String.valueOf(dataSent));
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
        long dataReceived = Utils.calcResponseHeaderSize(connection) + htmlTransferred;
        boolean isKeepAlive = Utils.isKeepAlive(connection);

        result.put(
            CommonConstant.SERVER_SOFTWARE, connection.getHeaderField(CommonConstant.SERVER));
        result.put(CommonConstant.SERVER_HOST, connection.getURL().getHost());
        result.put(CommonConstant.SERVER_PORT, String.valueOf(obj.getDefaultPort()));
        result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
        result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
        result.put(CommonConstant.LATENCY, String.valueOf(latency));
        result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
        result.put(CommonConstant.DATA_RECEIVED, String.valueOf(dataReceived));
        result.put(CommonConstant.DATA_SENT, String.valueOf(dataSent));
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
