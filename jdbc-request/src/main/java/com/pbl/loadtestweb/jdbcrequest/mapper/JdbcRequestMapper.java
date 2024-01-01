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
  @Mapping(source = "nameDBMS", target = "nameDBMS")
  @Mapping(source = "versionDBMS", target = "versionDBMS")
  @Mapping(source = "errorCode", target = "errorCode")
  @Mapping(source = "errorMessage", target = "errorMessage")
  @Mapping(source = "contentType", target = "contentType")
  @Mapping(source = "dataEncoding", target = "dataEncoding")
  @Mapping(source = "loadTime", target = "loadTime")
  @Mapping(source = "connectTime", target = "connectTime")
  @Mapping(source = "latency", target = "latency")
  @Mapping(source = "dataReceived",target = "dataReceived")
  @Mapping(source = "dataSent",target = "dataSent")
  @Mapping(source = "data", target = "data")
  JdbcDataResponse toJdbcDataResponse(
      String threadName,
      String iterations,
      String startAt,
      String nameDBMS,
      String versionDBMS,
      String errorCode,
      String errorMessage,
      String contentType,
      String dataEncoding,
      String loadTime,
      String connectTime,
      String latency,
      String dataSent,
      String dataReceived,
      List<JsonNode> data);
}
