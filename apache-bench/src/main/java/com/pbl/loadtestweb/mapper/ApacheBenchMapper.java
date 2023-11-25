package com.pbl.loadtestweb.mapper;

import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface ApacheBenchMapper {
  @Mapping(source = "serverSoftware", target = "serverSoftware")
  @Mapping(source = "serverHost", target = "serverHost")
  @Mapping(source = "serverPort", target = "serverPort")
  @Mapping(source = "concurrentLevel", target = "concurrentLevel")
  @Mapping(source = "timeTakenForTest", target = "timeTakenForTest")
  @Mapping(source = "completeRequest", target = "completeRequest")
  @Mapping(source = "failRequest", target = "failRequest")
  @Mapping(source = "non2xxResponse", target = "non2xxResponse")
  @Mapping(source = "keepAliveRequest", target = "keepAliveRequest")
  @Mapping(source = "totalTransferred", target = "totalTransferred")
  @Mapping(source = "htmlTransferred", target = "htmlTransferred")
  @Mapping(source = "requestPerSec", target = "requestPerSec")
  ApacheBenchResponse toApacheBenchResponse(
      String serverSoftware,
      String serverHost,
      String serverPort,
      String concurrentLevel,
      String timeTakenForTest,
      String completeRequest,
      String failRequest,
      String non2xxResponse,
      String keepAliveRequest,
      String totalTransferred,
      String htmlTransferred,
      String requestPerSec);
}
