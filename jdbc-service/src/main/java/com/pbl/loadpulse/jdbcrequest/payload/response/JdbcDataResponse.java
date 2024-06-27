package com.pbl.loadpulse.jdbcrequest.payload.response;

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
public class JdbcDataResponse {

  private Object threadName;

  private Object iterations;

  private Object startAt;

  private Object nameDBMS;

  private Object versionDBMS;

  private Object loadTime;

  private Object connectTime;

  private Object latency;

  private Object dataSent;

  private Object dataReceived;

  private Object errorCode;

  private Object errorMessage;

  private Object contentType;

  private Object data;
}
