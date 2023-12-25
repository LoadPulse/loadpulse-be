package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.httprequest.payload.request.HttpPostRequest;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/http-methods")
@Api(tags = "Http Request APIs")
public class HttpController {

  private final HttpRequestService httpRequestService;

  @GetMapping(value = "/get")
  public ResponseEntity<SseEmitter> sendHttpRequest(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "token", defaultValue = "") String token) {
    if (rampUp == 0) {
      return ResponseEntity.ok(httpRequestService.httpGet(url, threadCount, iterations, token));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpGetWithRampUp(url, threadCount, iterations, rampUp, token));
    }
  }

  @PostMapping("/{method}/mvc")
  public ResponseEntity<SseEmitter> sendHttpRequestWithEncodedFormBody(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @PathVariable String method,
      @RequestBody HttpPostRequest httpPostRequest) {
    if (rampUp == 0) {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestEncodedFormBody(
              url, threadCount, iterations, httpPostRequest, method));
    } else {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestEncodedFormBodyWithRampUp(
              url, threadCount, iterations, rampUp, httpPostRequest, method));
    }
  }

  @PostMapping("/{method}/api")
  public ResponseEntity<SseEmitter> sendHttpRequestWithJsonBody(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "token", defaultValue = "") String token,
      @PathVariable String method,
      @RequestBody HttpPostRequest httpPostRequest) {
    if (rampUp == 0) {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestJsonBody(
              url, threadCount, iterations, httpPostRequest, token, method));
    } else {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestJsonBodyWithRampUp(
              url, threadCount, iterations, rampUp, httpPostRequest, token, method));
    }
  }
}
