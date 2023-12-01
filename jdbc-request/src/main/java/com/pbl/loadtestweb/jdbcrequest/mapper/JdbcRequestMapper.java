package com.pbl.loadtestweb.jdbcrequest.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapStructConfig.class)
public interface JdbcRequestMapper {
  @Mapping(source = "threadName", target = "threadName")
  @Mapping(source = "iterations", target = "iterations")
  @Mapping(source = "startAt", target = "startAt")
  @Mapping(source = "responseCode", target = "responseCode")
  @Mapping(source = "responseMessage", target = "responseMessage")
  @Mapping(source = "contentType", target = "contentType")
  @Mapping(source = "dataEncoding", target = "dataEncoding")
  @Mapping(source = "loadTime", target = "loadTime")
  @Mapping(source = "connectTime", target = "connectTime")
  @Mapping(source = "latency", target = "latency")
  @Mapping(source = "headerSize", target = "headerSize")
  @Mapping(source = "bodySize", target = "bodySize")
  @Mapping(source = "data", target = "data")
  JdbcDataResponse toJdbcDataResponse(
      String threadName,
      String iterations,
      String startAt,
      String responseCode,
      String responseMessage,
      String contentType,
      String dataEncoding,
      String loadTime,
      String connectTime,
      String latency,
      String headerSize,
      String bodySize,
      List<JsonNode> data);
}
