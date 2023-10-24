package com.pbl.loadtestweb.jdbcrequest.payload.response;

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

public class JdbcDataResponse {

        private String threadName;

        private String iterations;

        private String startAt;

        private String loadTime;

        private String connectTime;

        private String latency;

        private String headerSize;

        private String bodySize;

        private String responseCode;

        private String responseMessage;

        private String contentType;

        private String dataEncoding;

        private String requestMethod;

}
