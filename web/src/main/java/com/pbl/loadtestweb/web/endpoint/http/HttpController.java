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

  @GetMapping(value = "/get/http/{url}")
  public ResponseEntity<SseEmitter> handleMethodGetHttp(
      @PathVariable String url,
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations) {
    url = CommonConstant.HTTP + url;
    return ResponseEntity.ok(
        httpRequestService.httpGet(url, threadCount, iterations, CommonConstant.HTTP_METHOD_GET));
  }

  @GetMapping("/get/https/{url}")
  public ResponseEntity<SseEmitter> handleMethodGetHttps(
      @PathVariable String url,
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations) {
    url = CommonConstant.HTTPS + url;
    return ResponseEntity.ok(
        httpRequestService.httpGet(url, threadCount, iterations, CommonConstant.HTTP_METHOD_GET));
  }

  @PostMapping("/post/mvc/http/{url}")
  public ResponseEntity<SseEmitter> handleMethodPostMVCHttp(
      @PathVariable String url,
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "path", required = false) String path,
      @RequestBody HttpPostRequest httpPostRequest) {
    if (path == null || path.isEmpty()) {
      url = CommonConstant.HTTP + url;
    } else {
      url = CommonConstant.HTTP + url + "/" + path;
    }

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

  @PostMapping("/post/mvc/https/{url}")
  public ResponseEntity<SseEmitter> handleMethodPostMVCHttps(
      @PathVariable String url,
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "path", required = false) String path,
      @RequestBody HttpPostRequest httpPostRequest) {

    if (path == null || path.isEmpty()) {
      url = CommonConstant.HTTPS + url;
    } else {
      url = CommonConstant.HTTPS + url + "/" + path;
    }

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

  @PostMapping("/post/api/http/{url}")
  public ResponseEntity<SseEmitter> handleMethodPostAPIHttp(
          @PathVariable String url,
          @RequestParam(name = "threads", defaultValue = "1") int threadCount,
          @RequestParam(name = "iterations", defaultValue = "1") int iterations,
          @RequestParam(name = "path", required = false) String path,
          @RequestBody HttpPostRequest httpPostRequest) {
    if (path == null || path.isEmpty()) {
      url = CommonConstant.HTTP + url;
    } else {
      url = CommonConstant.HTTP + url + "/" + path;
    }

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

  @PostMapping("/post/api/https/{url}")
  public ResponseEntity<SseEmitter> handleMethodPostAPIHttps(
          @PathVariable String url,
          @RequestParam(name = "threads", defaultValue = "1") int threadCount,
          @RequestParam(name = "iterations", defaultValue = "1") int iterations,
          @RequestParam(name = "path", required = false) String path,
          @RequestBody HttpPostRequest httpPostRequest) {

    if (path == null || path.isEmpty()) {
      url = CommonConstant.HTTPS + url;
    } else {
      url = CommonConstant.HTTPS + url + "/" + path;
    }

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
