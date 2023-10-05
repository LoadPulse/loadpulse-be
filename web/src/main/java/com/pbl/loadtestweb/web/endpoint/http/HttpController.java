package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.common.payload.general.ResponseDataAPI;
import com.pbl.loadtestweb.httprequest.service.HttpRequestService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/http-methods")
@Api(tags = "Http Request APIs")
public class HttpController {

  private final HttpRequestService httpRequestService;

  @GetMapping("/get/{url}")
  public ResponseEntity<ResponseDataAPI> handleMethodGetLoadTestWeb(@PathVariable String url) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(httpRequestService.handleMethodGetLoadTestWeb(url)));
  }
}
