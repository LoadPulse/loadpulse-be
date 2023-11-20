package com.pbl.loadtestweb.web.endpoint.apachebench;

import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;
import com.pbl.loadtestweb.service.ApacheBenchService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apache-bench")
@Api(tags = "Apache Bench APIs")
public class ApacheBenchController {

  private final ApacheBenchService apacheBenchService;

  @GetMapping("/default")
  public ResponseEntity<ApacheBenchResponse> loadTestABDefaultWithParams(
      @RequestParam(name = "requests", defaultValue = "1", required = true) int requests,
      @RequestParam(name = "concurrent", defaultValue = "1", required = true) int concurrent,
      @RequestParam(name = "path", defaultValue = "") String path) {
    return ResponseEntity.ok(
        apacheBenchService.loadTestABDefaultWithParams(requests, concurrent, path));
  }
}
