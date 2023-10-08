package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.common.constant.CommonConstant;
import com.pbl.loadtestweb.common.payload.general.ResponseDataAPI;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/http-methods")
@Api(tags = "Http Request APIs")
public class HttpController {

  private final HttpRequestService httpRequestService;

  @GetMapping(value = "/get/http/{url}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public ResponseEntity<SseEmitter> handleMethodGetHttp(
      @PathVariable String url,
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations) {
    url = CommonConstant.HTTP + url;
    return ResponseEntity.ok(
        httpRequestService.handleMethodGetLoadTestWeb(url, threadCount, iterations));
  }

  @GetMapping("/get/https/{url}")
  public ResponseEntity<ResponseDataAPI> handleMethodGetHttps(
      @PathVariable String url,
      @RequestParam(name = "threads", defaultValue = "1") int threadCount,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations) {
    url = CommonConstant.HTTPS + url;
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            httpRequestService.handleMethodGetLoadTestWeb(url, threadCount, iterations)));
  }
}
