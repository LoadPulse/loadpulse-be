package com.pbl.loadtestweb.jdbcrequest.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JdbcDataResponse {

  private String threadName;

  private String iterations;

  private String startAt;

  private String nameDBMS;

  private String versionDBMS;

  private String loadTime;

  private String connectTime;

  private String latency;

  private String dataSent;

  private String dataReceived;

  private String errorCode;

  private String errorMessage;

  private String contentType;

  private String dataEncoding;

  private List<JsonNode> data;
}
