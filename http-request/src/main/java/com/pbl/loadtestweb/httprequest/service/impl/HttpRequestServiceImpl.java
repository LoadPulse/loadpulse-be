package com.pbl.loadtestweb.httprequest.service.impl;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.common.exception.InternalServerException;
import com.pbl.loadtestweb.httprequest.mapper.HttpRequestMapper;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HttpRequestServiceImpl implements HttpRequestService {

  private final HttpRequestMapper httpRequestMapper;

  @Override
  public HttpDataResponse handleMethodGetLoadTestWeb(String url) {
    Map<String, String> result = this.loadTestThread(url, CommonConstant.HTTP_METHOD_GET);
    return buildHttpDataResponse(result);
  }

  private HttpDataResponse buildHttpDataResponse(Map<String, String> result) {
    return httpRequestMapper.toHttpDataResponse(
        result.get("threadName"),
        result.get("startAt"),
        result.get("responseCode"),
        result.get("responseMessage"),
        result.get("contentType"),
        result.get("dataEncoding"),
        result.get("requestMethod"),
        result.get("loadTime"),
        result.get("connectTime"),
        result.get("latency"),
        result.get("headerSize"),
        result.get("bodySize"));
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

  public Map<String, String> loadTestThread(String url, String method) {
    try {
      Map<String, String> result = new HashMap<>();
      URL obj = new URL(url);

      long startTime = System.currentTimeMillis();

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

      long endTime = System.currentTimeMillis();

      long latency = endTime - startTime;
      long connectTime = connection.getHeaderFieldLong("Date", 0) - startTime;
      long loadTime = endTime - connection.getHeaderFieldLong("Date", 0);

      result.put("loadTime", String.valueOf(loadTime));
      result.put("connectTime", String.valueOf(connectTime));
      result.put("latency", String.valueOf(latency));
      result.put("headerSize", String.valueOf(this.calcHeaderSize(connection)));
      result.put("bodySize", String.valueOf(this.calcBodySize(connection)));
      result.put("Sample Count", "1");
      result.put("Error Count", "0");
      result.put("Data type", "text");
      result.put("startAt", CommonFunction.formatDateToString(CommonFunction.getCurrentDateTime()));
      result.put("threadName", Thread.currentThread().getName());
      result.put("responseCode", Integer.toString(connection.getResponseCode()));
      result.put("responseMessage", connection.getResponseMessage());
      result.put("contentType", connection.getContentType());
      result.put("dataEncoding", connection.getContentEncoding());
      result.put("requestMethod", connection.getRequestMethod());

      return result;
    } catch (Exception e) {
      throw new InternalServerException(e.getMessage());
    }
  }
}
