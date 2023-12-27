package com.pbl.loadtestweb.httprequest.service;

import com.pbl.loadtestweb.httprequest.payload.request.HttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {
  SseEmitter sendHttpRequest(
      String url, int threadCount, int iterations, HttpRequest httpRequest, String method);

  SseEmitter sendHttpRequestWithRampUp(
      String url,
      int threadCount,
      int iterations,
      int rampUp,
      HttpRequest httpRequest,
      String method);

  SseEmitter sendHttpRequestEncodedFormBody(
      String url, int threadCount, int iterations, HttpRequest httpRequest, String method);

  SseEmitter sendHttpRequestEncodedFormBodyWithRampUp(
      String url,
      int threadCount,
      int iterations,
      int rampUp,
      HttpRequest httpRequest,
      String method);

  SseEmitter sendHttpRequestJsonBody(
      String url, int threadCount, int iterations, HttpRequest httpRequest, String method);

  SseEmitter sendHttpRequestJsonBodyWithRampUp(
      String url,
      int threadCount,
      int iterations,
      int rampUp,
      HttpRequest httpRequest,
      String method);
}
