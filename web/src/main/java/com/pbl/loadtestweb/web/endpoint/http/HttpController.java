package com.pbl.loadtestweb.web.endpoint.http;

import com.pbl.loadtestweb.common.constant.CommonConstant;
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

  @GetMapping("/get/http/{url}")
  public ResponseEntity<ResponseDataAPI> handleMethodGetHttp(@PathVariable String url) {
    url = CommonConstant.HTTP + url;
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(httpRequestService.handleMethodGetLoadTestWeb(url)));
  }

  @GetMapping("/get/https/{url}")
  public ResponseEntity<ResponseDataAPI> handleMethodGetHttps(@PathVariable String url) {
    url = CommonConstant.HTTPS + url;
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(httpRequestService.handleMethodGetLoadTestWeb(url)));
  }
}
