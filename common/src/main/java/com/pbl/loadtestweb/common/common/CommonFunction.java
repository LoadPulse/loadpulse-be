package com.pbl.loadtestweb.common.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CommonFunction {
  CommonFunction() {}

  public static Timestamp getCurrentDateTime() {
    Date date = new Date();
    return new Timestamp(date.getTime());
  }

  public static String formatDateToString(Timestamp timestamp) {
    SimpleDateFormat dateFormat;
    dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return dateFormat.format(timestamp);
  }
}
