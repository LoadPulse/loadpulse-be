package com.pbl.loadtestweb.web.endpoint.proxy;

import com.pbl.loadtestweb.proxy.payload.response.ProxyResponse;
import com.pbl.loadtestweb.proxy.service.ProxyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/proxy")
@RequiredArgsConstructor
@Api("Proxy APIs")
public class ProxyController {

  private final ProxyService proxyService;

  @GetMapping()
  public ResponseEntity<ProxyResponse> handleProxy() {
    return ResponseEntity.ok(proxyService.proxyBrowser());
  }
}
