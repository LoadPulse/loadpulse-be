package com.pbl.loadtestweb.httprequest.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {
  SseEmitter handleMethodGetLoadTestWeb(String url, int threadCount, int iterations);
}
