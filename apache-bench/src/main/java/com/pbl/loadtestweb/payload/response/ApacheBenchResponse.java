package com.pbl.loadtestweb.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApacheBenchResponse {

  private String serverSoftware;

  private String serverHost;

  private String serverPort;

  private String concurrentLevel;

  private String timeTakenForTest;

  private String completeRequest;

  private String failRequest;

  private String non2xxResponse;

  private String keepAliveRequest;

  private String totalTransferred;

  private String htmlTransferred;

  private String requestPerSec;
}
