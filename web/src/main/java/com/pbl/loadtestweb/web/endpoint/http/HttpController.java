package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.httprequest.payload.request.HttpRequest;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

  @PostMapping(value = "/{method}")
  @ApiOperation("Api send http request")
  public ResponseEntity<SseEmitter> sendHttpRequest(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "durations", defaultValue = "0") int durations,
      @RequestBody HttpRequest httpRequest,
      @PathVariable String method){
    if (rampUp == 0) {
      if (iterations == 0) {
        return ResponseEntity.ok(
            httpRequestService.sendHttpRequestWithDurations(
                url, virtualUsers, durations, httpRequest, method));
      } else {
        return ResponseEntity.ok(
            httpRequestService.sendHttpRequest(url, virtualUsers, iterations, httpRequest, method));
      }
    } else {
      if (iterations == 0) {
        return ResponseEntity.ok(
            httpRequestService.sendHttpRequestWithDurationsAndRampUp(
                url, virtualUsers, durations, rampUp, httpRequest, method));
      } else {
        return ResponseEntity.ok(
            httpRequestService.sendHttpRequestWithRampUp(
                url, virtualUsers, iterations, rampUp, httpRequest, method));
      }
    }
  }

  @PostMapping("/{method}/encoded-form-body")
  @ApiOperation("Api send http request with encoded form body")
  public ResponseEntity<SseEmitter> sendHttpRequestWithEncodedFormBody(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @PathVariable String method,
      @RequestBody HttpRequest httpRequest) {
    if (rampUp == 0) {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestEncodedFormBody(
              url, virtualUsers, iterations, httpRequest, method));
    } else {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestEncodedFormBodyWithRampUp(
              url, virtualUsers, iterations, rampUp, httpRequest, method));
    }
  }

  @PostMapping("/{method}/json-body")
  @ApiOperation("Api send http request with json body")
  public ResponseEntity<SseEmitter> sendHttpRequestWithJsonBody(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @PathVariable String method,
      @RequestBody HttpRequest httpRequest) {
    if (rampUp == 0) {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestJsonBody(
              url, virtualUsers, iterations, httpRequest, method));
    } else {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestJsonBodyWithRampUp(
              url, virtualUsers, iterations, rampUp, httpRequest, method));
    }
  }
}
