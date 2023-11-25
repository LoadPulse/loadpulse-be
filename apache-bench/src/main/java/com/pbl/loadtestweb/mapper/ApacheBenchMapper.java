package com.pbl.loadtestweb.mapper;

import com.pbl.loadtestweb.common.config.SpringMapStructConfig;
import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface ApacheBenchMapper {
  @Mapping(source = "serverSoftware", target = "serverSoftware")
  ApacheBenchResponse toApacheBenchResponse(String serverSoftware);
}
