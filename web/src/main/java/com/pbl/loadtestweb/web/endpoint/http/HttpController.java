package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.annotation.CurrentUser;
import com.pbl.loadtestweb.common.payload.general.ResponseDataAPI;
import com.pbl.loadtestweb.domain.UserPrincipal;
import com.pbl.loadtestweb.httprequest.payload.request.HttpRequest;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import com.pbl.loadtestweb.service.MessageQueueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/http-methods")
@Api(tags = "Http Request APIs")
public class HttpController {

  private final HttpRequestService httpRequestService;

  private final MessageQueueService messageQueueService;

  @PostMapping(value = "/{method}")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation("Api send http request")
  @ApiResponse(code = 200, message = "Create ")
  public ResponseEntity<ResponseDataAPI> sendHttpRequest(
      @RequestParam(name = "virtual_users", defaultValue = "1") int virtualUsers,
      @RequestParam(name = "iterations", defaultValue = "1") int iterations,
      @RequestParam(name = "url", defaultValue = "") String url,
      @RequestParam(name = "ramp_up", defaultValue = "0") int rampUp,
      @RequestParam(name = "durations", defaultValue = "0") int durations,
      @RequestParam(name = "is_keep_alive", defaultValue = "false") boolean isKeepAlive,
      @RequestBody HttpRequest httpRequest,
      @Parameter(description = "Method of test", required = true) @PathVariable
          String method,
      @ApiIgnore  @CurrentUser UserPrincipal userPrincipal) {
    String queueName = messageQueueService.createQueue(userPrincipal);
    if (iterations == 0) {
      httpRequestService.sendHttpRequestWithDurations(
          url, virtualUsers, durations, rampUp, isKeepAlive, httpRequest, method, userPrincipal);
    } else {
      httpRequestService.sendHttpRequest(
          url, virtualUsers, iterations, rampUp, isKeepAlive, httpRequest, method, userPrincipal);
    }
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(queueName));
  }

  @PostMapping("/{method}/encoded-form-body")
  @ApiOperation("Api send http request with encoded form body")
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
  @ApiOperation("Api send http request with json body")
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
