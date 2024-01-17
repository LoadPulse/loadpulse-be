package com.pbl.loadtestweb.jdbcrequest.mapper;

import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.jdbcrequest.payload.response.JdbcDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
  @Mapping(source = "loadTime", target = "loadTime")
  @Mapping(source = "connectTime", target = "connectTime")
  @Mapping(source = "latency", target = "latency")
  @Mapping(source = "dataReceived",target = "dataReceived")
  @Mapping(source = "dataSent",target = "dataSent")
  @Mapping(source = "data", target = "data")
  JdbcDataResponse toJdbcResponse(
      Object threadName,
      Object iterations,
      Object startAt,
      Object nameDBMS,
      Object versionDBMS,
      Object errorCode,
      Object errorMessage,
      Object contentType,
      Object loadTime,
      Object connectTime,
      Object latency,
      Object dataSent,
      Object dataReceived,
      Object data);
}
