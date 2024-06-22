package com.pbl.loadpulse.httprequest.service;

import com.pbl.loadpulse.httprequest.payload.request.HttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {

  void sendHttpRequest(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method);

  void sendHttpRequestWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method);

  SseEmitter sendHttpRequestEncodedFormBody(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method);

  SseEmitter sendHttpRequestEncodedFormBodyWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method);

  SseEmitter sendHttpRequestJsonBody(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method);

  SseEmitter sendHttpRequestJsonBodyWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method);
}
