package com.pbl.loadtestweb.common.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;

@MapperConfig(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpringMapStructConfig {}
