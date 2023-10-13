package com.pbl.loadtestweb.httprequest.service;

import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {
  SseEmitter handleLoadTestWeb(
      String url, int threadCount, int iterations, String method, HttpPostRequest httpPostRequest);
}
