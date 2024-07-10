package com.pbl.loadpulse.auth.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LogUtils {

  private LogUtils() {}

  public static void error(String method, String uri, String error) {
    String content = method + "/" + uri + " - " + "Error: " + error;
    log.error(content);
  }
}
