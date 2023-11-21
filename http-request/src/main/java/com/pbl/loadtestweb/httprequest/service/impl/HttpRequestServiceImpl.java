package com.pbl.loadtestweb.httprequest.service.impl;

import com.goebl.david.Webb;
import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.httprequest.mapper.HttpRequestMapper;
import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
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
        result.get(CommonConstant.BODY_SIZE));
  }

  private long calcBodySize(HttpURLConnection connection) throws IOException {

    InputStream responseStream = connection.getInputStream();
    ByteArrayOutputStream responseBodyStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = responseStream.read(buffer)) != -1) {
      responseBodyStream.write(buffer, 0, bytesRead);
    }
    return responseBodyStream.size();
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
      throw new RuntimeException(e);
    }
  }

  public Map<String, String> loadTestThread(String url, String method, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

      long connectStartTime = System.currentTimeMillis();

      connection.connect();

      long connectEndTime = System.currentTimeMillis();

      long loadStartTime = System.currentTimeMillis();

      InputStream inputStream = connection.getInputStream();
      long responseTime = 0;

      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }

      result.put(CommonConstant.RESPONSE_BODY, this.getResponseBody(connection));
      log.info(result.get(CommonConstant.RESPONSE_BODY));

      long loadEndTime = System.currentTimeMillis();

      long latency = responseTime - connectStartTime;
      long connectTime = connectEndTime - connectStartTime;
      long loadTime = loadEndTime - loadStartTime;

      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(this.calcHeaderSize(connection)));
      result.put(CommonConstant.BODY_SIZE, String.valueOf(this.calcBodySize(connection)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
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

  private String handleParamsToRequestBody(HttpPostRequest httpPostRequest) {
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

  public Map<String, String> loadTestThreadWithParamsMVC(
      String url, String method, HttpPostRequest httpPostRequest, int iterations) {
    Map<String, String> result = new HashMap<>();

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

      long connectStartTime = System.currentTimeMillis();

      connection.setDoInput(true);
      connection.setDoOutput(true);

      connection.connect();

      String requestBody = this.handleParamsToRequestBody(httpPostRequest);

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(requestBody);
      }

      long connectEndTime = System.currentTimeMillis();

      long loadStartTime = System.currentTimeMillis();

      InputStream inputStream = connection.getInputStream();
      long responseTime = 0;
      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }

      long loadEndTime = System.currentTimeMillis();

      long latency = responseTime - connectStartTime;
      long connectTime = connectEndTime - connectStartTime;
      long loadTime = loadEndTime - loadStartTime;

      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(this.calcHeaderSize(connection)));
      result.put(CommonConstant.BODY_SIZE, String.valueOf(this.calcBodySize(connection)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
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

      long connectStartTime = System.currentTimeMillis();

      connection.setDoInput(true);
      connection.setDoOutput(true);

      connection.connect();

      String requestBody = this.handleParamsToRequestBody(httpPostRequest);

      try (OutputStream os = connection.getOutputStream();
          OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
        writer.write(requestBody);
      }

      long connectEndTime = System.currentTimeMillis();

      long loadStartTime = System.currentTimeMillis();

      InputStream inputStream = connection.getInputStream();
      long responseTime = 0;
      if (inputStream != null) {
        responseTime = System.currentTimeMillis();
      }

      long loadEndTime = System.currentTimeMillis();

      long latency = responseTime - connectStartTime;
      long connectTime = connectEndTime - connectStartTime;
      long loadTime = loadEndTime - loadStartTime;

      result.put(CommonConstant.LOAD_TIME, String.valueOf(loadTime));
      result.put(CommonConstant.CONNECT_TIME, String.valueOf(connectTime));
      result.put(CommonConstant.LATENCY, String.valueOf(latency));
      result.put(CommonConstant.HEADER_SIZE, String.valueOf(this.calcHeaderSize(connection)));
      result.put(CommonConstant.BODY_SIZE, String.valueOf(this.calcBodySize(connection)));
      result.put(
          CommonConstant.START_AT,
          CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put(CommonConstant.THREAD_NAME, Thread.currentThread().getName());
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

  public void sendPOST() throws IOException {
    //    URL obj = new URL("https://api.superbad.store/auth/login");
    //    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    //    con.setRequestMethod("POST");
    //    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    //    con.setInstanceFollowRedirects(false);
    //
    //    String POST_PARAMS = "{\"email\":\"kinphan189@gmail.com\",\"password\":\"Phanhuy4\"}";
    //    // For POST only - START
    //    con.setDoOutput(true);
    //    OutputStream os = con.getOutputStream();
    //    os.write(POST_PARAMS.getBytes("UTF-8"));
    //    os.flush();
    //    os.close();
    //    // For POST only - END
    //    con.connect();
    //    int responseCode = con.getResponseCode();
    //    log.info("POST Response Code :: " + responseCode);
    //
    //    if (responseCode == HttpURLConnection.HTTP_OK) { // success
    //      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    //      String inputLine;
    //      StringBuffer response = new StringBuffer();
    //
    //      while ((inputLine = in.readLine()) != null) {
    //        response.append(inputLine);
    //      }
    //      in.close();
    //
    //      // print result
    //       System.out.println(response.toString());
    //    } else {
    //      System.out.println("POST request did not work.");
    //    }
    Webb webb = Webb.create();
    JSONObject result =
        webb.post("https://api.superbad.store/auth/login")
            .param("email", "kinphan189@gmail.com")
            .param("password", "Phanhuy4")
            .ensureSuccess()
            .asJsonObject()
            .getBody();

    System.out.println(result);
  }
}
