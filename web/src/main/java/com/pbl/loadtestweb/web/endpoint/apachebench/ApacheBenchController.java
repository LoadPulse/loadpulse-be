package com.pbl.loadtestweb.web.endpoint.apachebench;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apache-bench")
@Api(tags = "Apache Bench APIs")
public class ApacheBenchController {}
