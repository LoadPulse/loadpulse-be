package com.pbl.loadtestweb.httprequest.service.impl;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.exception.InternalServerException;
import com.pbl.loadtestweb.httprequest.mapper.HttpRequestMapper;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HttpRequestServiceImpl implements HttpRequestService {

  // private final HttpRequestMapper httpRequestMapper;

  @Override
  public HttpDataResponse handleMethodGetLoadTestWeb(String url) {
    Map<String, String> result = this.loadTestThread(url, "GET");
    return HttpDataResponse.builder()
        .startAt(result.get("startAt"))
        .threadName(result.get("threadName"))
        .responseCode(result.get("responseCode"))
        .responseMessage(result.get("responseMessage"))
        .contentType(result.get("contentType"))
        .dataEncoding(result.get("dataEncoding"))
        .requestMethod(result.get("requestMethod"))
        .build();
  }

  @Async("threadPoolTaskExecutor")
  public Map<String, String> loadTestThread(String url, String method) {
    try {
      Map<String, String> result = new HashMap<>();
      url = "http://" + url;
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod(method);

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
