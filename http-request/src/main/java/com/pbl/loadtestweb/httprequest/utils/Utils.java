package com.pbl.loadtestweb.httprequest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;

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
      throw new IllegalArgumentException("Unable to get response body", e);
    }
  }

  public static String getResponseBodyError(HttpURLConnection connection) {
    BufferedReader br = null;
    StringBuilder body = null;
    String line = "";
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

  public static String handleParamsToRequestBodyMVC(HttpPostRequest httpPostRequest) {
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

  public static String handleParamsToRequestBodyAPI(HttpPostRequest httpPostRequest) {
    Map<String, String> params = new HashMap<>();
    try {
      for (int i = 0; i < httpPostRequest.getKey().size(); i++) {
        params.put(httpPostRequest.getKey().get(i), httpPostRequest.getValue().get(i));
      }
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(params);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Unable to convert params to request body", e);
    }
  }

  public static long timeForCreationEachThread(int threadCount, int rampUp) {
    return (long) ((double) rampUp / threadCount * 1000);
  }

  public static int calcThreadIncrement(int targetThreadCount, int rampUpTimeInSeconds) {
    return Math.max(1, targetThreadCount / rampUpTimeInSeconds);
  }

  public static void sleepThread(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }
}
