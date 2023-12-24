package com.pbl.loadtestweb.httprequest.service;

import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {
  SseEmitter httpGet(String url, int threadCount, int iterations);

  SseEmitter httpGetWithRampUp(String url, int threadCount, int iterations, int rampUp);

  SseEmitter httpPostMVC(
      String url, int threadCount, int iterations, HttpPostRequest httpPostRequest);

  SseEmitter httpPostMVCWithRampUp(
      String url, int threadCount, int iterations, int rampUp, HttpPostRequest httpPostRequest);

  SseEmitter httpPostAPI(
      String url, int threadCount, int iterations, HttpPostRequest httpPostRequest);

  SseEmitter httpPostAPIWithRampUp(
      String url, int threadCount, int iterations, int rampUp, HttpPostRequest httpPostRequest);
}
