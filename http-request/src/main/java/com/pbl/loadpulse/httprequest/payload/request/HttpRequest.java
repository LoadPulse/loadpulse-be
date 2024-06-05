package com.pbl.loadpulse.httprequest.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HttpRequest {

  private List<String> keyBodies;

  private List<String> valueBodies;

  private List<String> keyHeaders;

  private List<String> valueHeaders;
}
