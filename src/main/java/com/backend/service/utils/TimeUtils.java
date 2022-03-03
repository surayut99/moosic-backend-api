package com.backend.service.utils;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeUtils {
  private static TimeUtils timeUtils;

  private TimeUtils() {
  }

  public static TimeUtils getInstance() {
    if (timeUtils == null) {
      timeUtils = new TimeUtils();
    }

    return timeUtils;
  }

  public String getMondayTimestampOfWeek() {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

//    return "2022-02-14 00:00:00.000";

    return new Timestamp(c.getTime().getTime()).toString();
  }

  public boolean isWeekEndDay() {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(Calendar.MONDAY);

    int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    return dow == 1 || dow == 6;
  }
}
