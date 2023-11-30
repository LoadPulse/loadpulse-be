package com.pbl.loadtestweb.common.constant;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class CommonConstant {
  private CommonConstant() {}

  public static final String BASE_PACKAGE_ENDPOINT = "com.pbl.loadtestweb.web";

  public static final String SUCCESS = "success";

  public static final String ERROR = "error";

  public static final String HTTP = "http://";

  public static final String HTTPS = "https://";

  public static final String HTTP_METHOD_GET = "GET";

  public static final String HTTP_METHOD_POST = "POST";

  public static final String HTTP_METHOD_PUT = "PUT";

  // Key
  public static final String LOAD_TIME = "loadTime";

  public static final String CONNECT_TIME = "connectTime";

  public static final String LATENCY = "latency";

  public static final String HEADER_SIZE = "headerSize";

  public static final String BODY_SIZE = "bodySize";

  public static final String START_AT = "startAt";

  public static final String THREAD_NAME = "threadName";

  public static final String ITERATIONS = "iterations";

  public static final String RESPONSE_CODE = "responseCode";

  public static final String RESPONSE_MESSAGE = "responseMessage";

  public static final String CONTENT_TYPE = "contentType";

  public static final String DATA_ENCODING = "dataEncoding";

  public static final String REQUEST_METHOD = "requestMethod";

  public static final String SERVER_SOFTWARE = "serverSoftware";

  public static final String SERVER_HOST = "serverHost";

  public static final String SERVER_PORT = "serverPort";

  public static final String CURRENT_LEVEL = "concurrentLevel";
  public static final String TIME_TAKE_FOR_TEST = "timeTakenForTest";
  public static final String COMPLETE_REQUEST = "completeRequest";
  public static final String FAIL_REQUEST = "failRequest";
  public static final String NON2XX_REQUEST = "non2xxResponse";
  public static final String KEEPALIVE_REQUEST = "keepAliveRequest";
  public static final String TOTAL_TRANSFERRED = "totalTransferred";
  public static final String HTML_TRANSFERRED = "htmlTransferred";
  public static final String REQUEST_PER_SEC = "requestPerSec";
  public static final String DATA = "data";
}
