package com.pbl.loadtestweb.jdbcrequest.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JdbcDataRequest {

   private String DatabaseURL;
   private String JdbcDriverClass;
   private String Username;
   private String Password;
   private String SqlStatements;
}