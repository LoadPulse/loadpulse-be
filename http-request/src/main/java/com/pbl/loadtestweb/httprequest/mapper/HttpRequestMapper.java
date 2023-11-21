package com.pbl.loadtestweb.httprequest.mapper;

import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
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
  @Mapping(source = "headerSize", target = "headerSize")
  @Mapping(source = "bodySize", target = "bodySize")
  @Mapping(source = "responseBody", target = "responseBody")
  HttpDataResponse toHttpDataResponse(
      String threadName,
      String iterations,
      String startAt,
      String responseCode,
      String responseMessage,
      String responseBody,
      String contentType,
      String dataEncoding,
      String requestMethod,
      String loadTime,
      String connectTime,
      String latency,
      String headerSize,
      String bodySize);
}
