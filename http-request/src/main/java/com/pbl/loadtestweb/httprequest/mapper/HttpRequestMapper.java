package com.pbl.loadtestweb.httprequest.mapper;

import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface HttpRequestMapper {

  @Mapping(source = "threadName", target = "threadName")
  @Mapping(source = "startAt", target = "startAt")
  @Mapping(source = "responseCode", target = "responseCode")
  @Mapping(source = "responseMessage", target = "responseMessage")
  @Mapping(source = "contentType", target = "contentType")
  @Mapping(source = "dataEncoding", target = "dataEncoding")
  @Mapping(source = "requestMethod", target = "requestMethod")
  HttpDataResponse toHttpDataResponse(
      String threadName,
      String startAt,
      String responseCode,
      String responseMessage,
      String contentType,
      String dataEncoding,
      String requestMethod);
}
