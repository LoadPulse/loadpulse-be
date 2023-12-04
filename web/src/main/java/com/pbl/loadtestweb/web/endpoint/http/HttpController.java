package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.common.constant.CommonConstant;
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

  @GetMapping(value = "/get/http")
  public ResponseEntity<SseEmitter> handleMethodGetHttp(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url) {
    url = CommonConstant.HTTP + url;
    return ResponseEntity.ok(
        httpRequestService.httpGet(url, threadCount, iterations, CommonConstant.HTTP_METHOD_GET));
  }

  @GetMapping("/get/https")
  public ResponseEntity<SseEmitter> handleMethodGetHttps(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url) {
    url = CommonConstant.HTTPS + url;
    return ResponseEntity.ok(
        httpRequestService.httpGet(url, threadCount, iterations, CommonConstant.HTTP_METHOD_GET));
  }

  @PostMapping("/post/mvc/http")
  public ResponseEntity<SseEmitter> handleMethodPostMVCHttp(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestBody HttpPostRequest httpPostRequest) {
    url = CommonConstant.HTTP + url;
    if (httpPostRequest.getKey().isEmpty()) {
      return ResponseEntity.ok(
          httpRequestService.httpGet(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpPostMVC(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST, httpPostRequest));
    }
  }

  @PostMapping("/post/mvc/https")
  public ResponseEntity<SseEmitter> handleMethodPostMVCHttps(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestBody HttpPostRequest httpPostRequest) {

    url = CommonConstant.HTTPS + url;

    if (httpPostRequest.getKey().isEmpty()) {
      return ResponseEntity.ok(
          httpRequestService.httpGet(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpPostMVC(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST, httpPostRequest));
    }
  }

  @PostMapping("/post/api/http")
  public ResponseEntity<SseEmitter> handleMethodPostAPIHttp(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestBody HttpPostRequest httpPostRequest) {
    url = CommonConstant.HTTP + url;

    if (httpPostRequest.getKey().isEmpty()) {
      return ResponseEntity.ok(
          httpRequestService.httpGet(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpPostAPI(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST, httpPostRequest));
    }
  }

  @PostMapping("/post/api/https")
  public ResponseEntity<SseEmitter> handleMethodPostAPIHttps(
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestBody HttpPostRequest httpPostRequest) {

    url = CommonConstant.HTTPS + url;

    if (httpPostRequest.getKey().isEmpty()) {
      return ResponseEntity.ok(
          httpRequestService.httpGet(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST));
    } else {
      return ResponseEntity.ok(
          httpRequestService.httpPostAPI(
              url, threadCount, iterations, CommonConstant.HTTP_METHOD_POST, httpPostRequest));
    }
  }
}
