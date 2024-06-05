package com.pbl.loadpulse.httprequest.mapper;

import com.pbl.loadpulse.common.config.SpringMapStructConfig;
import com.pbl.loadpulse.httprequest.payload.response.HttpDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface HttpRequestMapper {

  @Mapping(source = "threadName", target = "threadName")
  @Mapping(source = "iterations", target = "iterations")
  @Mapping(source = "startAt", target = "startAt")
  @Mapping(source = "responseCode", target = "responseCode")
  @Mapping(source = "responseMessage", target = "responseMessage")
  @Mapping(source = "contentType", target = "contentType")
  @Mapping(source = "dataEncoding", target = "dataEncoding")
  @Mapping(source = "requestMethod", target = "requestMethod")
  @Mapping(source = "loadTime", target = "loadTime")
  @Mapping(source = "connectTime", target = "connectTime")
  @Mapping(source = "latency", target = "latency")
  @Mapping(source = "responseBody", target = "responseBody")
  @Mapping(source = "serverSoftware", target = "serverSoftware")
  @Mapping(source = "serverHost", target = "serverHost")
  @Mapping(source = "serverPort", target = "serverPort")
  @Mapping(source = "keepAlive", target = "keepAlive")
  @Mapping(source = "dataReceived", target = "dataReceived")
  @Mapping(source = "dataSent", target = "dataSent")
  @Mapping(source = "responseHeader", target = "responseHeader")
  HttpDataResponse toHttpDataResponse(
      String serverSoftware,
      String serverHost,
      String serverPort,
      String threadName,
      String iterations,
      String startAt,
      String responseCode,
      String responseMessage,
      String responseHeader,
      String responseBody,
      String contentType,
      String dataEncoding,
      String requestMethod,
      String loadTime,
      String connectTime,
      String latency,
      String dataReceived,
      String dataSent,
      String keepAlive);
}
