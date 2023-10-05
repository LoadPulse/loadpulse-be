package com.pbl.loadtestweb.httprequest.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpDataResponse {

  private String threadName;

  private String startAt;

  private String loadTime;

  private String connectTime;

  private String latency;

  private String responseCode;

  private String responseMessage;

  private String contentType;

  private String dataEncoding;

  private String requestMethod;

  public static Builder builder() {
    return new Builder();
  }

  // Builder class
  public static class Builder {
    private final HttpDataResponse httpDataResponse = new HttpDataResponse();

    public Builder threadName(String threadName) {
      httpDataResponse.threadName = threadName;
      return this;
    }

    public Builder startAt(String startAt) {
      httpDataResponse.startAt = startAt;
      return this;
    }

    public Builder responseCode(String responseCode) {
      httpDataResponse.responseCode = responseCode;
      return this;
    }

    public Builder responseMessage(String responseMessage) {
      httpDataResponse.responseMessage = responseMessage;
      return this;
    }

    public Builder contentType(String contentType) {
      httpDataResponse.contentType = contentType;
      return this;
    }

    public Builder dataEncoding(String dataEncoding) {
      httpDataResponse.dataEncoding = dataEncoding;
      return this;
    }

    public Builder requestMethod(String requestMethod) {
      httpDataResponse.requestMethod = requestMethod;
      return this;
    }

    public HttpDataResponse build() {
      return httpDataResponse;
    }
  }
}
