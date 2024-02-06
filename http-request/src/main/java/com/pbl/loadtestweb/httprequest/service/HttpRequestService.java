package com.pbl.loadtestweb.httprequest.service;

import com.pbl.loadtestweb.domain.UserPrincipal;
import com.pbl.loadtestweb.httprequest.payload.request.HttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface HttpRequestService {

  void sendHttpRequest(
      String url,
      int virtualUsers,
      int iterations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method,
      UserPrincipal userPrincipal);

  void sendHttpRequestWithDurations(
      String url,
      int virtualUsers,
      int durations,
      int rampUp,
      boolean isKeepAlive,
      HttpRequest httpRequest,
      String method,
      UserPrincipal userPrincipal);

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
