package com.pbl.loadtestweb.service.impl;

import com.pbl.loadtestweb.common.common.CommonFunction;
import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.mapper.ApacheBenchMapper;
import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;
import com.pbl.loadtestweb.service.ApacheBenchService;
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
public class ApacheBenchServiceImpl implements ApacheBenchService {
  private final ApacheBenchMapper apacheBenchMapper;
  @Override
  public ApacheBenchResponse loadTestABDefaultWithParams(int request, int concurrent , String url) {
    Map<String,String> result = this.loadTestThread();
    return buildApacheBenchResponse(result);
  }
  private void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }
  private ApacheBenchResponse buildApacheBenchResponse(Map<String,String> result){
    return apacheBenchMapper.toApacheBenchResponse(
            result.get(CommonConstant.SERVER_SOFTWARE),
            result.get(CommonConstant.SERVER_HOST),
            result.get(CommonConstant.SERVER_PORT),
            result.get(CommonConstant.CURRENT_LEVEL),
            result.get(CommonConstant.TIME_TAKE_FOR_TEST),
            result.get(CommonConstant.COMPLETE_REQUEST),
            result.get(CommonConstant.FAIL_REQUEST),
            result.get(CommonConstant.NON2XX_REQUEST),
            result.get(CommonConstant.KEEPALIVE_REQUEST),
            result.get(CommonConstant.TOTAL_TRANSFERRED),
            result.get(CommonConstant.HTML_TRANSFERRED),
            result.get(CommonConstant.REQUEST_PER_SEC)
    );
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

  public Map<String, String> loadTestThread(int request, int concurrent,String url)  {
    Map<String, String> result = new HashMap<>();
    try{
    URL obj = new URL(url);

    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
    String serverHost = conn.getURL().getHost();
    String serverSoftWare = conn.getHeaderField("Server");
    int serverPost = obj.getPort();
    int n = request/concurrent + 1 ;

    result.put(CommonConstant.SERVER_SOFTWARE,serverSoftWare);
    result.put(CommonConstant.SERVER_HOST,serverHost);
    result.put(CommonConstant.SERVER_PORT,String.valueOf(serverPost));
    result.put(CommonConstant.CURRENT_LEVEL,String.valueOf(concurrent));
    } catch (IOException ignored)
    {

    }
  return result;
  }
}
