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
  public ResponseEntity<SseEmitter> handleMethodGet(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp) {
    if (rampUp == 0) {
      return ResponseEntity.ok(httpRequestService.httpGet(url, threadCount, iterations));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpGetWithRampUp(url, threadCount, iterations, rampUp));
    }
  }

  @PostMapping("/post/mvc")
  public ResponseEntity<SseEmitter> handleMethodPostMVC(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", required = false) int rampUp,
      @RequestBody HttpPostRequest httpPostRequest) {
    if (httpPostRequest.getKey().isEmpty()) {
      return ResponseEntity.ok(httpRequestService.httpGet(url, threadCount, iterations));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpPostMVC(url, threadCount, iterations, httpPostRequest));
    }
  }

  @PostMapping("/post/api")
  public ResponseEntity<SseEmitter> handleMethodPostAPI(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", required = false) int rampUp,
      @RequestBody HttpPostRequest httpPostRequest) {
    if (httpPostRequest.getKey().isEmpty()) {
      return ResponseEntity.ok(httpRequestService.httpGet(url, threadCount, iterations));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpPostAPI(url, threadCount, iterations, httpPostRequest));
    }
  }
}
