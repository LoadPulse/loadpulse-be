package com.pbl.loadtestweb.service.impl;

import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.mapper.ApacheBenchMapper;
import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;
import com.pbl.loadtestweb.service.ApacheBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ApacheBenchServiceImpl implements ApacheBenchService {
  private final ApacheBenchMapper apacheBenchMapper;
  private final ExecutorService executorService = Executors.newCachedThreadPool();

  @Override
  public ApacheBenchResponse loadTestABDefaultWithParams(int request, int concurrent, String url) {
    Map<String, String> result = this.loadTestThread(request, concurrent, url);
    return buildApacheBenchResponse(result);
  }

  private ApacheBenchResponse buildApacheBenchResponse(Map<String, String> result) {
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
        result.get(CommonConstant.REQUEST_PER_SEC));
  }

  private long calcTotalSize(HttpURLConnection connection) throws IOException {
    Map<String, List<String>> responseHeaders = connection.getHeaderFields();
    long headersSize = 0;

    for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
      String headerName = entry.getKey();
      List<String> headerValues = entry.getValue();

      for (String headerValue : headerValues) {
        headersSize += (headerName + ": " + headerValue + "\r\n").getBytes().length;
      }
    }
    InputStream responseStream = connection.getInputStream();
    ByteArrayOutputStream responseBodyStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = responseStream.read(buffer)) != -1) {
      responseBodyStream.write(buffer, 0, bytesRead);
    }
    return headersSize + responseBodyStream.size();
  }

  public long calcNumberofNon2xxResponse( HttpURLConnection conn) {
    long non2xxResponseCount = 0;
      try {
        int responseCode = conn.getResponseCode();
        if (responseCode / 200 != 2) {
          non2xxResponseCount = 1;
        }
        conn.disconnect();

      } catch (IOException e) {
        non2xxResponseCount = 1;
      }
    return non2xxResponseCount;
  }

  //  public int calcNumberofCompleteRequest(int concurrent,HttpURLConnection conn) throws
  // IOException
  //  {
  //
  //  }
  public long calcNumberofKeepAliveRequest( HttpURLConnection conn) {
    long keepAliveRequest = 0;
      if (conn.getHeaderField("Connection") == "keep-alive") {
        keepAliveRequest=1;
      }
    return keepAliveRequest;
  }

  //  public long calcNumberofFailedRequest(int concurrent,String url,int port)
  //  {
  //
  //  }
  public Map<String, String> loadTestThread(int request, int concurrent, String url) {
    Map<String, String> result = new HashMap<>();
    try {
      URL obj = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
      String serverSoftWare = conn.getHeaderField("Server");
      String serverHost = conn.getURL().getHost();
      int serverPost = obj.getDefaultPort();
      result.put(CommonConstant.SERVER_SOFTWARE, serverSoftWare);
      result.put(CommonConstant.SERVER_HOST, serverHost);
      result.put(CommonConstant.SERVER_PORT, String.valueOf(serverPost));
      result.put(CommonConstant.CURRENT_LEVEL, String.valueOf(concurrent));
      conn.disconnect();
      long startTime = System.currentTimeMillis();

      int n;
      if (request % concurrent == 0) {
        n = request / concurrent;
      } else {
        n = request / concurrent + 1;
      }
      AtomicLong non2xxResponse = new AtomicLong();
      AtomicLong keepaliveRequest = new AtomicLong();
      AtomicLong totalSize = new AtomicLong();

      int req = request;
      for (int i = 1; i <= n; i++) {
        for(int j = 0; j < concurrent; j++) {
          executorService.execute(
                  ()->{
                    URL obj1 = null;
                    try {
                      obj1 = new URL(url);
                    } catch (MalformedURLException e) {
                      throw new RuntimeException(e);
                    }
                    HttpURLConnection conn1 = null;
                    try {
                      conn1 = (HttpURLConnection) obj1.openConnection();
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                    non2xxResponse.addAndGet(calcNumberofNon2xxResponse(conn1));
                    keepaliveRequest.addAndGet(calcNumberofKeepAliveRequest(conn1));
                    //totalSize.addAndGet(calcTotalSize(conn1));
                  });
        }
        req -= concurrent;
        if (req < concurrent) {
          concurrent = req;
        }
      }
      long endTime = System.currentTimeMillis();
      long timeTakeForTest = endTime - startTime;
      double requestPerSec = timeTakeForTest / request;
      DecimalFormat df = new DecimalFormat("#.####");

      result.put(CommonConstant.COMPLETE_REQUEST, "0");
      result.put(CommonConstant.FAIL_REQUEST, "0");
      result.put(CommonConstant.NON2XX_REQUEST, String.valueOf(non2xxResponse.get()));
      result.put(CommonConstant.KEEPALIVE_REQUEST, String.valueOf(keepaliveRequest.get()));
      result.put(CommonConstant.TOTAL_TRANSFERRED, String.valueOf(totalSize.get()));
      result.put(CommonConstant.TIME_TAKE_FOR_TEST, String.valueOf(timeTakeForTest));
      result.put(CommonConstant.REQUEST_PER_SEC, String.valueOf(df.format(requestPerSec)));
      result.put(CommonConstant.HTML_TRANSFERRED, "0");
    } catch (IOException ignored) {

    }
    return result;
  }
}
