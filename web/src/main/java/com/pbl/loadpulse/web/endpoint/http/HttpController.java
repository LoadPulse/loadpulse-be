package com.pbl.loadpulse.web.endpoint.http;

import com.pbl.loadpulse.common.payload.general.ResponseDataAPI;
import com.pbl.loadpulse.httprequest.payload.request.HttpRequest;
import com.pbl.loadpulse.httprequest.service.HttpRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/http-methods")
@Tag(name = "Http Request APIs")
public class HttpController {

  private final HttpRequestService httpRequestService;

  @PostMapping(value = "/{method}")
  @PreAuthorize("hasRole('USER')")
  @Operation(summary = "Api send http request")
  public ResponseEntity<ResponseDataAPI> sendHttpRequest(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "durations", defaultValue = "0") int durations,
      @RequestParam(name = "is_keep_alive", defaultValue = "false") boolean isKeepAlive,
      @RequestBody HttpRequest httpRequest,
      @Parameter(description = "Method of test", required = true) @PathVariable String method) {
    if (iterations == 0) {
      httpRequestService.sendHttpRequestWithDurations(
          url, virtualUsers, durations, rampUp, isKeepAlive, httpRequest, method);
    } else {
      httpRequestService.sendHttpRequest(
          url, virtualUsers, iterations, rampUp, isKeepAlive, httpRequest, method);
    }
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }

  @PostMapping("/{method}/encoded-form-body")
  @Operation(summary = "Api send http request with encoded form body")
  public ResponseEntity<SseEmitter> sendHttpRequestWithEncodedFormBody(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "durations", defaultValue = "0") int durations,
      @RequestParam(name = "is_keep_alive", defaultValue = "false") boolean isKeepAlive,
      @PathVariable String method,
      @RequestBody HttpRequest httpRequest) {
    if (iterations == 0) {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestEncodedFormBodyWithDurations(
              url, virtualUsers, durations, rampUp, isKeepAlive, httpRequest, method));
    } else {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestEncodedFormBody(
              url, virtualUsers, iterations, rampUp, isKeepAlive, httpRequest, method));
    }
  }

  @PostMapping("/{method}/json-body")
  @Operation(summary = "Api send http request with json body")
  public ResponseEntity<SseEmitter> sendHttpRequestWithJsonBody(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "durations", defaultValue = "0") int durations,
      @RequestParam(name = "is_keep_alive", defaultValue = "false") boolean isKeepAlive,
      @PathVariable String method,
      @RequestBody HttpRequest httpRequest) {
    if (iterations == 0) {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestJsonBodyWithDurations(
              url, virtualUsers, durations, rampUp, isKeepAlive, httpRequest, method));
    } else {
      return ResponseEntity.ok(
          httpRequestService.sendHttpRequestJsonBody(
              url, virtualUsers, iterations, rampUp, isKeepAlive, httpRequest, method));
    }
  }
}
