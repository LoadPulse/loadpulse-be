package com.pbl.loadpulse.httprequest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.loadpulse.httprequest.payload.request.HttpRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
  Utils() {}

  public static long calcResponseHeaderSize(HttpURLConnection connection) {
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

  public static String getResponseHeaders(HttpURLConnection connection) {
    Map<String, List<String>> responseHeaders = connection.getHeaderFields();
    StringBuilder headersStringBuilder = new StringBuilder();

    for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
      String headerName = entry.getKey();
      List<String> headerValues = entry.getValue();

      for (String headerValue : headerValues) {
        headersStringBuilder.append(headerName).append(": ").append(headerValue).append("\r\n");
      }
    }

    return headersStringBuilder.toString();
  }

  public static long calcRequestHeaderSize(HttpURLConnection connection) {
    Map<String, List<String>> responseHeaders = connection.getRequestProperties();
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

  public static String getResponseBodySuccess(HttpURLConnection connection) {
    BufferedReader br;
    StringBuilder body;
    String line;
    try {
      br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      body = new StringBuilder();
      while ((line = br.readLine()) != null) {
        body.append(line);
      }
      return body.toString();
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to get response body", e);
    }
  }

  public static String getResponseBodyError(HttpURLConnection connection) {
    BufferedReader br;
    StringBuilder body;
    String line;
    try {
      br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      body = new StringBuilder();
      while ((line = br.readLine()) != null) {
        body.append(line);
      }
      return body.toString();
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to get response body", e);
    }
  }

  public static boolean isKeepAlive(HttpURLConnection connection) {
    String connectionHeader = connection.getHeaderField("Connection");
    return "keep-alive".equalsIgnoreCase(connectionHeader);
  }

  public static String handleParamsToEncodedFormBody(HttpRequest httpPostRequest) {
    Map<String, String> params = new HashMap<>();
    for (int i = 0; i < httpPostRequest.getKeyBodies().size(); i++) {
      params.put(httpPostRequest.getKeyBodies().get(i), httpPostRequest.getValueBodies().get(i));
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

  public static String handleParamsToJsonBody(HttpRequest httpPostRequest) {
    Map<String, String> params = new HashMap<>();
    try {
      for (int i = 0; i < httpPostRequest.getKeyBodies().size(); i++) {
        params.put(httpPostRequest.getKeyBodies().get(i), httpPostRequest.getValueBodies().get(i));
      }
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(params);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Unable to convert params to request body", e);
    }
  }

  public static long timeForCreationEachThread(int threadCount, int rampUp) {
    if (rampUp == 0) {
      return 0;
    }
    return (long) ((double) rampUp / threadCount * 1000);
  }

  public static void sleepThread(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  public static HttpURLConnection setRequestProperties(
      HttpURLConnection connection, HttpRequest httpRequest) {
    for (int i = 0; i < httpRequest.getKeyHeaders().size(); i++) {
      if (httpRequest.getKeyHeaders().get(i).length() != 0
          && httpRequest.getValueHeaders().get(i).length() != 0) {
        connection.setRequestProperty(
            httpRequest.getKeyHeaders().get(i), httpRequest.getValueHeaders().get(i));
      }
    }
    return connection;
  }
}
