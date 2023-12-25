package com.pbl.loadtestweb.httprequest.service;

import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {
  SseEmitter httpGet(String url, int threadCount, int iterations, String token);

  SseEmitter httpGetWithRampUp(
      String url, int threadCount, int iterations, int rampUp, String token);

  SseEmitter sendHttpRequestEncodedFormBody(
      String url, int threadCount, int iterations, HttpPostRequest httpPostRequest, String method);

  SseEmitter sendHttpRequestEncodedFormBodyWithRampUp(
      String url,
      int threadCount,
      int iterations,
      int rampUp,
      HttpPostRequest httpPostRequest,
      String method);

  SseEmitter sendHttpRequestJsonBody(
      String url,
      int threadCount,
      int iterations,
      HttpPostRequest httpPostRequest,
      String token,
      String method);

  SseEmitter sendHttpRequestJsonBodyWithRampUp(
      String url,
      int threadCount,
      int iterations,
      int rampUp,
      HttpPostRequest httpPostRequest,
      String token,
      String method);
}
