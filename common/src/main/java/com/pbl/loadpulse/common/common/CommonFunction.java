package com.pbl.loadpulse.common.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.loadpulse.common.payload.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
public final class CommonFunction {
  CommonFunction() {}

  private static final String ERROR_FILE = "errors.yml";

  private static final String VALIDATION_FILE = "validations.yml";

  public static Timestamp getCurrentDateTime() {
    Date date = new Date();
    return new Timestamp(date.getTime());
  }

  public static String formatDateToString(Timestamp timestamp) {
    SimpleDateFormat dateFormat;
    dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return dateFormat.format(timestamp);
  }

  public static ErrorResponse getExceptionError(String error) {
    ReadYAML readYAML = new ReadYAML();
    Map<String, Object> errors = readYAML.getValueFromYAML(ERROR_FILE);
    Map<String, Object> objError = (Map<String, Object>) errors.get(error);
    String code = (String) objError.get("code");
    String message = (String) objError.get("message");
    return new ErrorResponse(code, message);
  }

  public static ErrorResponse getValidationError(
      String resource, String fieldName, String validation) {
    ReadYAML readYAML = new ReadYAML();
    Map<String, Object> errors = readYAML.getValueFromYAML(VALIDATION_FILE);
    Map<String, Object> fields = (Map<String, Object>) errors.get(resource);
    Map<String, Object> objErrors = (Map<String, Object>) fields.get(fieldName);
    Map<String, Object> objError = (Map<String, Object>) objErrors.get(validation);
    String code = (String) objError.get("code");
    String message = (String) objError.get("message");
    return new ErrorResponse(code, message);
  }

  public static String convertToJSONString(Object ob) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(ob);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

  public static String convertToSnakeCase(String input) {
    return input.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
  }

  public static UUID generateUUID() {
    return UUID.randomUUID();
  }
}
