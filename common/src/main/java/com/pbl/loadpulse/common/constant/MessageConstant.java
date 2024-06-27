package com.pbl.loadpulse.common.constant;

public class MessageConstant {

  private MessageConstant() {}

  public static final String INTERNAL_SERVER_ERROR = "internal_server_error";

  // Auth
  public static final String EMAIL_ALREADY_EXISTS = "email_already_exists";

  public static final String INCORRECT_EMAIL_OR_PASSWORD = "incorrect_email_or_password";

  public static final String PASSWORD_NOT_MATCHED_WITH_CONFIRM_PASSWORD =
      "password_and_confirmation_password_is_not_match";

  public static final String UNAUTHORIZED = "unauthorized";

  public static final String FORBIDDEN = "forbidden";

  public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";

  public static final String REVOKED_TOKEN = "revoked_token";

  public static final String EXPIRED_TOKEN = "expired_token";

  public static final String OAUTH_TOKEN_NOT_FOUND = "oauth_token_not_found";

  public static final String INVALID_TOKEN = "invalid_token";

  public static final String EXPIRED_REFRESH_TOKEN = "expired_refresh_token";

  // User
  public static final String USER_NOT_FOUND = "user_not_found";
}
